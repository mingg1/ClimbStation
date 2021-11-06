package fi.metropolia.climbstation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import fi.metropolia.climbstation.R

class ClimbingProgressActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_climbing_progress)
setChartData()
    }

    private fun setChartData(){
        val chart = findViewById<LineChart>(R.id.chart_current_climbing)
        val valueList = ArrayList<Double>()
        val entries = ArrayList<Entry>()
        val title = "example"

        for(i in 0..6){
            valueList.add(i * 100.1)
        }

        for (i in 0 until valueList.size){
            val barEntry = BarEntry(i.toFloat(), valueList.get(i).toFloat())
            entries.add(barEntry)
        }

        val dataSet = LineDataSet(entries, title)
        val data = LineData(dataSet)
chart.data = data
        chart.invalidate()
    }
}