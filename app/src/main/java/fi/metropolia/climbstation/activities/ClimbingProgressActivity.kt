package fi.metropolia.climbstation.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.ActivityClimbingProgressBinding
import kotlinx.coroutines.launch
import fi.metropolia.climbstation.network.ClimbStationRepository
import fi.metropolia.climbstation.network.ClimbStationViewModel
import fi.metropolia.climbstation.network.ClimbStationViewModelFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingProgressBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.buttonPause.setOnClickListener { startStopTimer() }
        binding.buttonFinish.setOnClickListener { stopTimer() }
        binding.textDifficultyMode.text = intent.extras?.getString("mode")

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        startStopTimer()


        lateinit var viewModel: ClimbStationViewModel
        val repository = ClimbStationRepository()
        val viewModelFactory = ClimbStationViewModelFactory(repository)
        val clientKey = intent.extras?.getString("clientKey")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)
        lifecycleScope.launch {
//            Log.d("----------", "--------------------------------")
//
//            val inforeq = InfoRequest("20110001", clientKey!!)
//            val res = repository.getInfo(inforeq)
//            Log.d("infores", "$res")
//            viewModel.infoResponse.value = res
//            viewModel.infoResponse.observe(
//                this@ClimbingProgressActivity,
//                { res -> Log.d("info", "$res" ?: "none") })
        }
        setChartData()
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
            binding.textConsumedCalory.text = consumedCalories.toString()
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

    private fun setChartData() {
        val chart = findViewById<LineChart>(R.id.chart_current_climbing)
        val valueList = ArrayList<Double>()
        val entries = ArrayList<Entry>()
        val title = "example"

        for (i in 0..6) {
            valueList.add(i * 100.1)
        }

        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList.get(i).toFloat())
            entries.add(barEntry)
        }

        val dataSet = LineDataSet(entries, title)
        val data = LineData(dataSet)
        chart.data = data
        chart.setGridBackgroundColor(R.color.black)
        chart.invalidate()
    }
}