package fi.metropolia.climbstation.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.ActivityClimbingProgressBinding
import fi.metropolia.climbstation.network.*
import kotlinx.coroutines.launch
import fi.metropolia.climbstation.service.TimerService
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
    val viewModelFactory = ClimbStationViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingProgressBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val clientKey = intent.extras?.getString("clientKey")
        val climbMode = intent.extras?.getString("mode")
        val speed = intent.extras?.getString("speed")
        val totalLength = intent.extras?.getString("length")
        Log.d("keys","$clientKey $climbMode $speed $totalLength}")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)
        binding.buttonPause.setOnClickListener { startStopTimer() }
        binding.buttonFinish.setOnClickListener {
            stopTimer()
            lifecycleScope.launch {
                val stopReq = OperationRequest("20110001", clientKey!!, "stop")
                val operationRes = repository.setOperation(stopReq)
                viewModel.operationResponse.value = operationRes
            }
        }
        binding.textDifficultyMode.text = intent.extras?.getString("mode")

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        startStopTimer()

        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post(object : Runnable {
            override fun run() {
                lifecycleScope.launch {
                    Log.d("----------", "--------------------------------")

                    val infoReq = InfoRequest("20110001", clientKey!!)
                    val res = repository.getInfo(infoReq)
                    Log.d("infores", "$res")
                    viewModel.infoResponse.value = res
                    viewModel.infoResponse.observe(
                        this@ClimbingProgressActivity,
                        { res ->
                            Log.d("info", "$res" ?: "none")
                            binding.textClimbedLength.text = res.length
                        })
                }
                mHandler.postDelayed(this, 1000)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        resetTimer()
        unregisterReceiver(updateTime)
    }

    // Calories burned per second = (MET x body weight in Kg x 3.5) ÷ 200 ÷ 60
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

    private fun startStopTimer() {
        if (timerStarted) stopTimer()
        else startTimer()
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