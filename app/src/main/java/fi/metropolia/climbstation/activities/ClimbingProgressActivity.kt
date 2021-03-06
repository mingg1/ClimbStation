package fi.metropolia.climbstation.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingProgressBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.service.TimerService
import fi.metropolia.climbstation.util.Config
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

/**
 * Activity to handle climbing progress
 *
 * @author Minji Choi
 *
 */
class ClimbingProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingProgressBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    lateinit var climbStationViewModel: ClimbStationViewModel
    private lateinit var terrainProfileViewModel: TerrainProfileViewModel
    var consumedCalories = 0.0
    private var time = 0.0
    var weight = 60
    private var levelTotalLength: Int = 0
    private var climbedLength = 0
    private var completedStepLength = 0
    private var currentStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingProgressBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val repository = ClimbStationRepository(Config(this).baseUrl!!)
        val viewModelFactory = ClimbStationViewModelFactory(repository)
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        terrainProfileViewModel = ViewModelProvider(this)[TerrainProfileViewModel::class.java]
        val mHandler = Handler(Looper.getMainLooper())

        var vibrationEffect1: VibrationEffect
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator

        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        val sf = getSharedPreferences("climbStation", MODE_PRIVATE)
        weight = sf.getInt("weight", weight)
        val serialNumber = sf.getString("serialNumber", "")
        val clientKey = intent.extras?.getString("clientKey")
        val currentLevelText = intent.extras?.getString("difficultyLevel")
        val speed = intent.extras?.getString("speed")
        val goalLength = intent.extras?.getString("length")
        val climbMode = intent.extras?.getString("mode")

        var currentLevel = terrainProfileViewModel.getTerrainProfileByName(currentLevelText!!)
        val currentLevelStacks = currentLevel.phases
        currentLevelStacks.forEach { levelTotalLength += it.distance }
        var currentAngle = currentLevelStacks[currentStep].angle

        binding.textAngle.text = getString(R.string.degree, currentAngle)
        binding.textGoalLength.text = goalLength
        if (currentLevelStacks.size > 1) {
            binding.textNextAngle.text =
                getString(R.string.degree, currentLevelStacks[currentStep + 1].angle)
            binding.textNextDistance.text =
                getString(R.string.distance, currentLevelStacks[currentStep + 1].distance)
        } else {
            binding.textNextInfo.visibility = View.GONE
            binding.nextAngleContainer.visibility = View.GONE
            binding.nextDurationContainer.visibility = View.GONE
        }
        // send http request every second
        val postRequestRunnable = object : Runnable {
            override fun run() {
                mHandler.removeCallbacks(this)
                lifecycleScope.launch {
                    val infoReq = InfoRequest(serialNumber!!, clientKey!!)
                    val res = repository.getInfo(infoReq)
                    climbStationViewModel.infoResponse.value = res
                }
                mHandler.postDelayed(this, 1000)
            }
        }

        climbStationViewModel.infoResponse.observe(
            this@ClimbingProgressActivity,
            {
                try {
                   climbedLength = it.length.toInt()
//                   climbedLength += 1
                    binding.textClimbedLength.text = climbedLength.toString()
                    binding.lengthProgressBar.apply {
                        progressMax = goalLength!!.toFloat()
                        progress = climbedLength.toFloat()
                    }
                    if (climbedLength - completedStepLength >= currentLevelStacks[currentStep].distance) {
                        if (currentStep == currentLevelStacks.size - 1) currentStep = 0
                        else currentStep++
                        completedStepLength = climbedLength

                        currentAngle = currentLevelStacks[currentStep].angle
                        val angleReq =
                            AngleRequest(serialNumber!!, clientKey!!, currentAngle.toString())
                        lifecycleScope.launch { repository.setAngle(angleReq) }

                        binding.textAngle.text = getString(R.string.degree, currentAngle)
                        binding.textNextAngle.text = getString(
                            R.string.degree,
                            if (currentStep == currentLevelStacks.size - 1) currentLevelStacks[0].angle else currentLevelStacks[currentStep + 1].angle
                        )
                        binding.textNextDistance.text = getString(
                            R.string.distance,
                            if (currentStep == currentLevelStacks.size - 1) currentLevelStacks[0].distance else currentLevelStacks[currentStep + 1].distance
                        )
                    }
                    currentLevel = nextDifficultyLevel(
                        climbedLength,
                        levelTotalLength,
                        currentLevel,
                        climbMode!!
                    )

                    if (climbedLength >= goalLength!!.toInt()) {
                        stopTimer()
                        if (mHandler.post(postRequestRunnable)) stopRunnable(
                            mHandler,
                            postRequestRunnable
                        )
                        lifecycleScope.launch {
                            val stopReq =
                                OperationRequest(serialNumber!!, clientKey!!, "stop")
                            val operationRes = repository.setOperation(stopReq)
                            climbStationViewModel.operationResponse.value = operationRes
                        }
                        val intent =
                            Intent(
                                this@ClimbingProgressActivity,
                                ClimbingResultsActivity::class.java
                            )
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

                        // vibrates the phone when the climb is finished
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrationEffect1 = VibrationEffect.createOneShot(
                                1000,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                            vibrator.vibrate(vibrationEffect1)
                        } else {
                            // backward compatibility for Android API < 26
                            vibrator.vibrate(1000)
                        }
                        startActivity(intent)
                        finishAffinity()
                    }

                } catch (err: NumberFormatException) {
                    Toast.makeText(
                        applicationContext, "No internet connection!", Toast.LENGTH_SHORT
                    ).show()
                }
            })

        fun checkResponse(response: MutableLiveData<Response<ClimbStationResponse>>): Boolean {
            var isOkay = false
            response.observe(this, {
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
                        val stopReq = OperationRequest(serialNumber!!, clientKey!!, "stop")
                        val operationRes = repository.setOperation(stopReq)
                        climbStationViewModel.operationResponse.value = operationRes
                    } else {
                        val speedReq = SpeedRequest(serialNumber!!, clientKey!!, speed!!)
                        climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)
                        val angleReq = AngleRequest(
                            serialNumber,
                            clientKey,
                            currentLevelStacks[currentStep].angle.toString()
                        )
                        climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)
                        val startReq = OperationRequest(serialNumber, clientKey, "start")
                        climbStationViewModel.operationResponse.value =
                            repository.setOperation(startReq)
                    }
                }.await()
                startStopTimer(mHandler, postRequestRunnable)
            }
        }

        binding.buttonFinish.setOnClickListener {
            stopTimer()
            if (mHandler.post(postRequestRunnable)) stopRunnable(mHandler, postRequestRunnable)
            lifecycleScope.launch {
                async {
                    val stopReq = OperationRequest(serialNumber!!, clientKey!!, "stop")
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


    // Calories burnt per second = (MET x body weight in Kg x 3.5) ?? 200 ?? 60
    private fun calculateCalories(weight: Int, time: Double): Double =
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
        binding.buttonPause.text = getString(R.string.pause)
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
        binding.buttonPause.text = getString(R.string.resume)
        timerStarted = false
    }

    // calculate total length and move on to the next level(s)
    private fun nextDifficultyLevel(
        climbedLength: Int,
        levelTotalLength: Int,
        currentLevel: TerrainProfile, mode: String
    ): TerrainProfile {
        var currentStack: TerrainProfile = currentLevel
        if (climbedLength == levelTotalLength) {
            val currentIndex = currentStack.id
            val index: Long = when (mode) {
                "To next level" -> (if (currentIndex.toInt() == terrainProfileViewModel.getTerrainProfiles().size) 1 else currentIndex + 1)
                "Random" -> (1..terrainProfileViewModel.getTerrainProfiles().size).random().toLong()
                else -> currentIndex
            }
            currentStack = terrainProfileViewModel.getTerrainProfileById(index)
        }
        return currentStack
    }
}
