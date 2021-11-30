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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.TerrainProfilesObject
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingProgressBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.service.TimerService
import fi.metropolia.climbstation.util.Constants.Companion.SERIAL_NUM
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ClimbingProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingProgressBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    lateinit var climbStationViewModel: ClimbStationViewModel
    private lateinit var terrainProfileViewModel: TerrainProfileViewModel
    val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)
    var consumedCalories = 0.0
    private var time = 0.0
    var weight = 60.0 // in kg
    private var levelTotalLength: Int = 0
    var climbedLength = 0
    var completedStepLength = 0
    var currentStep = 0

    var totalClimbedLength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingProgressBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        terrainProfileViewModel = ViewModelProvider(this)[TerrainProfileViewModel::class.java]
        val mHandler = Handler(Looper.getMainLooper())

        val clientKey = intent.extras?.getString("clientKey")
        val currentLevelText = intent.extras?.getString("difficultyLevel")
        val speed = intent.extras?.getString("speed")
        val goalLength = intent.extras?.getString("length")
        val climbMode = intent.extras?.getString("mode")

        var currentLevel = terrainProfileViewModel.getTerrainProfileByName(currentLevelText!!)
//        var currentLevel = TerrainProfilesObject.terrainProfiles.find { it.name == currentLevelText }
        val currentLevelStacks = currentLevel.phases
//        currentLevelStacks!!.forEach { levelTotalLength += it.distance }
        currentLevelStacks.forEach { levelTotalLength += it.distance }
        var currentAngle = currentLevelStacks[0].angle
//            currentLevelStacks?.get(0)?.angle

//        Log.d("level", aaa.toString())
        binding.textAngle.text = getString(R.string.degree, currentAngle)

        val postRequestRunnable = object : Runnable {
            override fun run() {
                mHandler.removeCallbacks(this)
                lifecycleScope.launch {
                    val infoReq = InfoRequest(SERIAL_NUM, clientKey!!)
                    val res = repository.getInfo(infoReq)
                    climbStationViewModel.infoResponse.value = res
                }
                mHandler.postDelayed(this, 1000)
            }
        }

        climbStationViewModel.infoResponse.observe(
            this@ClimbingProgressActivity,
            { res ->
                try {
                    climbedLength = res.length.toInt()
                    totalClimbedLength += climbedLength
                    binding.textClimbedLength.text = totalClimbedLength.toString()
                    binding.lengthProgressBar.apply {
                        progressMax = goalLength!!.toFloat()
                        progress = totalClimbedLength.toFloat()
                    }
                    if (climbedLength >= currentLevelStacks[currentStep].distance) {
                        if (currentStep == currentLevelStacks.size - 1) currentStep = 0
                        else currentStep++
//                        completedStepLength = climbedLength

                        currentAngle = currentLevelStacks[currentStep].angle
                        val angleReq =
                            AngleRequest(SERIAL_NUM, clientKey!!, currentAngle.toString())
                        lifecycleScope.launch { repository.setAngle(angleReq) }

                        binding.textAngle.text = getString(R.string.degree, currentAngle)
                    }
                    currentLevel = nextDifficultyLevel(
                        climbedLength,
                        levelTotalLength,
                        currentLevel,
                        climbMode!!
                    )
                    binding.textDifficultyMode.text = currentLevel.name
//                    Log.d("val", "${currentStep},$levelTotalLength,$climbedLength")
                    if (totalClimbedLength >= goalLength!!.toInt()) {
                        stopTimer()
                        if (mHandler.post(postRequestRunnable)) stopRunnable(
                            mHandler,
                            postRequestRunnable
                        )
                        lifecycleScope.launch {
                            val stopReq =
                                OperationRequest(SERIAL_NUM, clientKey!!, "stop")
                            val operationRes = repository.setOperation(stopReq)
                            climbStationViewModel.operationResponse.value = operationRes
                        }
                        val intent =
                            Intent(
                                this@ClimbingProgressActivity,
                                ClimbingResultsActivity::class.java
                            )
                        intent.putExtra("climbedLength", totalClimbedLength)
                        intent.putExtra("goalLength", goalLength)
                        intent.putExtra("durationText", getTimeString(time))
                        intent.putExtra("duration", time)
                        intent.putExtra("speed", speed?.toInt())
                        intent.putExtra("level", currentLevel!!.name)
                        intent.putExtra("calories", consumedCalories)
                        intent.putExtra("clientKey", clientKey)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finishAffinity()
                    }

                } catch (err: NumberFormatException) {
                    Toast.makeText(
                        applicationContext,
                        "No internet connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        fun checkResponse(response: MutableLiveData<Response<ClimbStationResponse>>): Boolean {
            var isOkay = false
            response.observe(this, { it ->
                if (it != null && it.body()?.response != "NOTOK") {
                    isOkay = true
                }
            })
            return isOkay
        }

        binding.buttonPause.setOnClickListener {
            lifecycleScope.launch {
                async {
                    if (timerStarted) {
                        val stopReq = OperationRequest(SERIAL_NUM, clientKey!!, "stop")
                        val operationRes = repository.setOperation(stopReq)
                        climbStationViewModel.operationResponse.value = operationRes
                    } else {
                        val speedReq = SpeedRequest(SERIAL_NUM, clientKey!!, speed!!)
                        climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)
                        val angleReq = AngleRequest(
                            SERIAL_NUM,
                            clientKey,
                            currentLevelStacks[currentStep].angle.toString()
                        )
                        climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)
                        val startReq = OperationRequest(SERIAL_NUM, clientKey, "start")
                        climbStationViewModel.operationResponse.value =
                            repository.setOperation(startReq)
                    }
                }.await()
                startStopTimer(mHandler, postRequestRunnable)
            }
        }

        binding.textDifficultyMode.text = currentLevelText

        binding.buttonFinish.setOnClickListener {
            stopTimer()
            if (mHandler.post(postRequestRunnable)) stopRunnable(mHandler, postRequestRunnable)
            lifecycleScope.launch {
                async {
                    val stopReq = OperationRequest(SERIAL_NUM, clientKey!!, "stop")
                    val operationRes = repository.setOperation(stopReq)
                    climbStationViewModel.operationResponse.value = operationRes
                }.await()

                val intent =
                    Intent(this@ClimbingProgressActivity, ClimbingResultsActivity::class.java)
                intent.putExtra("climbedLength", climbedLength)
                intent.putExtra("goalLength", goalLength)
                intent.putExtra("durationText", getTimeString(time))
                intent.putExtra("duration", time)
                intent.putExtra("speed", speed?.toInt())
                intent.putExtra("level", currentLevel.name)
                intent.putExtra("calories", consumedCalories)
                intent.putExtra("clientKey", clientKey)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finishAffinity()
                resetTimer()
            }
        }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        startStopTimer(mHandler, postRequestRunnable)
    }

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
            consumedCalories = calculateCalories(weight, time)
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

    private fun nextDifficultyLevel(
        climbedLength: Int,
        levelTotalLength: Int,
        currentLevel: TerrainProfile, mode: String
    ): TerrainProfile {
        var currentStack: TerrainProfile = currentLevel
        if (climbedLength == levelTotalLength) {
            val currentIndex = currentStack.id.toInt()
            Log.d("index", currentIndex.toString())
            val index: Int = when (mode) {
                "To next level" -> (if (currentIndex == terrainProfileViewModel.getTerrainProfiles().size) 1 else currentIndex + 1)
                "Random" -> (1..terrainProfileViewModel.getTerrainProfiles().size).random()
                else -> currentIndex
            }
            currentStack = terrainProfileViewModel.getTerrainProfileById(index)
        }
//            indexOf(currentLevel)
        Log.d("change", currentStack.name)
        return currentStack
    }
}
