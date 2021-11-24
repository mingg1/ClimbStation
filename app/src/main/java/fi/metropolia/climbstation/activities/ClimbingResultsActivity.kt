package fi.metropolia.climbstation.activities

import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.ActivityClimbResultBinding
import fi.metropolia.climbstation.network.ClimbStationRepository
import fi.metropolia.climbstation.network.LogOutRequest
import fi.metropolia.climbstation.util.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ClimbingResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbResultBinding
    private val repository = ClimbStationRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbResultBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val clientKey = intent.extras?.getString("clientKey")
        val climbedLength = intent.extras?.getInt("climbedLength")
        val goalLength = intent.extras?.getString("goalLength")?.toInt()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val duration = simpleDateFormat.format(Calendar.getInstance().timeInMillis)
binding.resultsMsg.text = getString(R.string.results_message,climbedLength,goalLength)
        binding.textDateValue.text = duration
        binding.textBurntCaloriesValue.text = getString(R.string.consumed_calories,intent.extras?.getDouble("calories"))
        binding.textLengthValue.text = climbedLength.toString()
        binding.textDifficultyLevelValue.text = intent.extras?.getString("level")

        binding.buttonGoToHistory.setOnClickListener { }
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