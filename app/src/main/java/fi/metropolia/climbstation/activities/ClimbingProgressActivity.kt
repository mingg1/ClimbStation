package fi.metropolia.climbstation.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import fi.metropolia.climbstation.R
import kotlinx.coroutines.launch
import network.ClimbStationRepository
import network.ClimbStationViewModel
import network.ClimbStationViewModelFactory
import network.InfoRequest

class ClimbingProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_climbing_progress)

        lateinit var viewModel: ClimbStationViewModel
        val repository = ClimbStationRepository()
        val viewModelFactory = ClimbStationViewModelFactory(repository)
        val clientKey = intent.extras?.getString("clientKey")
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)
        lifecycleScope.launch {
            Log.d("----------", "--------------------------------")

            val inforeq = InfoRequest("20110001", clientKey!!)
            val res = repository.getInfo(inforeq)
            Log.d("infores", "$res")
            viewModel.infoResponse.value = res
            viewModel.infoResponse.observe(
                this@ClimbingProgressActivity,
                { res -> Log.d("info", "$res" ?: "none") })
        }

        setChartData()
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
        chart.invalidate()
    }
}