package fi.metropolia.climbstation.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.TerrainProfiles
import fi.metropolia.climbstation.databinding.ActivityClimbingProgressBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.service.TimerService
import fi.metropolia.climbstation.util.Constants.Companion.SERIAL_NUM
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ClimbingProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingProgressBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    var weight = 60.0 // in kg
    lateinit var viewModel: ClimbStationViewModel
    val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)

    var climbedLength = 0
    var completedAreaLength = 0
    var currentStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingProgressBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)

        val clientKey = intent.extras?.getString("clientKey")
        val difficultyLevel = intent.extras?.getString("difficultyLevel")
        val speed = intent.extras?.getString("speed")
        val goalLength = intent.extras?.getString("length")
        val climbMode = intent.extras?.getString("mode")
        val currentLevel = TerrainProfiles.difficultyLevels.find { it.name == difficultyLevel }
        val currentLevelStacks = currentLevel?.profiles
        var currentAngle = currentLevelStacks?.get(0)?.angle
        var levelTotalLength = 0
        currentLevelStacks?.forEach { it -> levelTotalLength += it.distance }
        binding.textAngle.text = currentAngle.toString()
        val mHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                lifecycleScope.launch {
                    val infoReq = InfoRequest(SERIAL_NUM, clientKey!!)
                    val res = repository.getInfo(infoReq)
                    viewModel.infoResponse.value = res
                    viewModel.infoResponse.observe(
                        this@ClimbingProgressActivity,
                        { res ->
                            try {
                                climbedLength = res.length.toInt()
                                binding.textClimbedLength.text = climbedLength.toString()

                                if (climbedLength - completedAreaLength == currentLevelStacks!![currentStep].distance) {
                                    if (currentStep == currentLevelStacks.size - 1) currentStep = 0
                                    else currentStep++
                                    completedAreaLength = climbedLength

                                    currentAngle = currentLevelStacks[currentStep].angle
                                    val angleReq = AngleRequest(
                                        SERIAL_NUM,
                                        clientKey,
                                        currentAngle.toString()
                                    )
                                    lifecycleScope.launch { repository.setAngle(angleReq) }
                                    Log.d("currentStep", "$res")
                                    binding.textAngle.text = currentAngle.toString()
                                }
                            } catch (err: NumberFormatException) {
                                Toast.makeText(
                                    applicationContext,
                                    "No internet connection!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
                mHandler.postDelayed(this, 1000)
            }
        }
        // startRunnable(mHandler,runnable)

        binding.textDifficultyMode.text = difficultyLevel
        binding.buttonPause.setOnClickListener {
            startStopTimer(mHandler, runnable)
        }
        binding.buttonFinish.setOnClickListener {
            stopTimer()
            if (mHandler.post(runnable)) stopRunnable(mHandler, runnable)
            lifecycleScope.launch {
                val stopReq = OperationRequest(SERIAL_NUM, clientKey!!, "stop")
                val operationRes = repository.setOperation(stopReq)
                viewModel.operationResponse.value = operationRes
            }
            val intent = Intent(this@ClimbingProgressActivity, ClimbingResultsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
        }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        startStopTimer(mHandler, runnable)

    }

//    override fun onPause() {
//        super.onPause()
//        resetTimer()
//        unregisterReceiver(updateTime)
//    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    // Calories burnt per second = (MET x body weight in Kg x 3.5) ÷ 200 ÷ 60
    private fun calculateCalories(weight: Double, time: Double): Double =
        roundOffDecimal(5.8 * weight * time / 200 / 60)

    private fun roundOffDecimal(number: Double): Double {
        val format = DecimalFormat("#.##")
        format.roundingMode = RoundingMode.HALF_EVEN
        return format.format(number).toDouble()
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.textDuration.text = getTimeString(time)
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            time = intent?.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)!!
            binding.textDuration.text = getTimeString(time)
            val consumedCalories = calculateCalories(weight, time)
            binding.textConsumedCalory.text =
                getString(R.string.consumed_calories, consumedCalories)
        }

    }

    private fun startRunnable(handler: Handler, runnable: Runnable) {
        handler.post(runnable)
    }

    private fun stopRunnable(handler: Handler, runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }

    private fun startStopTimer(handler: Handler, runnable: Runnable) {
        if (timerStarted) {
            stopTimer()
            stopRunnable(handler, runnable)
        } else {
            startTimer()
            startRunnable(handler, runnable)
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.buttonPause.text = "Pause"
        timerStarted = true
    }

    private fun getTimeString(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return formatTimeString(hours, minutes, seconds)
    }

    private fun formatTimeString(hours: Int, minutes: Int, seconds: Int): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.buttonPause.text = "Resume"
        timerStarted = false
    }

}