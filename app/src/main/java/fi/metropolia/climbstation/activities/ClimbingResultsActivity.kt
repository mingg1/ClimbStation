package fi.metropolia.climbstation.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.viewModels.ClimbHistoryViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbResultBinding
import java.util.*


/**
 * Activity to show the status of complete climbing
 *
 * @author Minji Choi
 *
 */
class ClimbingResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbResultBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val climbHistoryHistoryViewModel: ClimbHistoryViewModel by viewModels()
        val difficultyLevel = intent.extras?.getString("level")
        val climbedLength = intent.extras?.getInt("climbedLength")
        val goalLength = intent.extras?.getString("goalLength")?.toInt()
        val dateNow = Calendar.getInstance().timeInMillis
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dateString = simpleDateFormat.format(dateNow)
        val speed = intent.extras?.getInt("speed")
        val burntCalories = intent.extras?.getDouble("calories")
        val durationText = intent.extras?.getString("durationText")

        binding.resultsMsg.text = getString(R.string.results_message, climbedLength, goalLength)
        binding.textSpeedValue.text = getString(R.string.speed, speed)
        binding.textDateValue.text = dateString
        binding.textBurntCaloriesValue.text =
            getString(R.string.consumed_calories, burntCalories)
        binding.textLengthValue.text = climbedLength.toString()
        binding.textDurationValue.text = durationText
        binding.textLengthValue.text = getString(R.string.distance, climbedLength)
        binding.textDifficultyLevelValue.text = difficultyLevel

        climbHistoryHistoryViewModel.addClimbHistory(
            ClimbHistory(
                0, dateNow,
                difficultyLevel!!, climbedLength!!, goalLength!!, durationText!!, speed!!, burntCalories!!
            )
        )

        binding.buttonGoToHistory.setOnClickListener {
            val intent = Intent(this@ClimbingResultsActivity, HistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
        }
        binding.buttonGoToMain.setOnClickListener {
            val intent = Intent(this@ClimbingResultsActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
        }

        binding.shareBtn.setOnClickListener{
            val intent = Intent().apply {
                intent.action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is the result of my Climb, Try your\'s!!")
                type ="text/plain"
            }
            startActivity(Intent.createChooser(intent,"Select an application to Share", null))
        }
    }
}