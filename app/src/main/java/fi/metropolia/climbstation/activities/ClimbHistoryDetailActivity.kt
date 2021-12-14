package fi.metropolia.climbstation.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.viewModels.ClimbHistoryViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbResultBinding
import java.util.*

class ClimbHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbResultBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val climbHistoryHistoryViewModel: ClimbHistoryViewModel by viewModels()
        val historyId = intent.extras?.getLong("historyId")
        val historyInfo = historyId?.let { climbHistoryHistoryViewModel.getClimbHistoryById(it) }
        val climbedDate = historyInfo?.dateTime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val climbedDateString = simpleDateFormat.format(climbedDate)

        binding.resultsMsg.text =
            getString(R.string.results_message, historyInfo?.climbedLength, historyInfo?.goalLength)
        binding.textSpeedValue.text = getString(R.string.speed, historyInfo?.speed)
        binding.textDateValue.text = climbedDateString
        binding.textBurntCaloriesValue.text =
            getString(R.string.consumed_calories, historyInfo?.burntCalories)
        binding.textLengthValue.text = historyInfo?.climbedLength.toString()
        binding.textDurationValue.text = historyInfo?.duration
        binding.textLengthValue.text = getString(R.string.distance, historyInfo?.climbedLength)
        binding.textDifficultyLevelValue.text = historyInfo?.level

        binding.buttonGoToMain.visibility = View.GONE
        binding.buttonGoToHistory.visibility = View.GONE

        binding.shareBtn.setOnClickListener {
            val intent = Intent().apply {
                intent.action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is the result of my Climb, Try your\'s!!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Select an application to Share", null))
        }
    }
}