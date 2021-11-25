package fi.metropolia.climbstation.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.Climb
import fi.metropolia.climbstation.database.viewModels.ClimbViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbResultBinding
import fi.metropolia.climbstation.network.ClimbStationRepository
import java.util.*

class ClimbingResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbResultBinding
    private val repository = ClimbStationRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbResultBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val climbHistoryViewModel: ClimbViewModel by viewModels()
        val clientKey = intent.extras?.getString("clientKey")
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
        binding.textLengthValue.text = getString(R.string.climbed_length_value, climbedLength)
        binding.textDifficultyLevelValue.text = difficultyLevel


        climbHistoryViewModel.addClimbHistory(
            Climb(
                0, dateNow,
                difficultyLevel!!, climbedLength!!, durationText!!, speed!!, burntCalories!!
            )
        )

        binding.buttonGoToHistory.setOnClickListener {
            val intent = Intent(this@ClimbingResultsActivity, HistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
        }
        binding.buttonGoToMain.setOnClickListener {
//            lifecycleScope.launch {
//                async{
//                    val logOutReq = LogOutRequest(Constants.SERIAL_NUM, clientKey!!)
//                    repository.logOut(logOutReq)
//                }.await()
//                val sharedPrefs: SharedPreferences = getSharedPreferences("climbStation", MODE_PRIVATE)
//                val editor = sharedPrefs.edit()
//                editor.clear()
//                editor.apply()
            val intent = Intent(this@ClimbingResultsActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
//        }
        }
    }
}