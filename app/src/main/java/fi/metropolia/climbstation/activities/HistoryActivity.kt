package fi.metropolia.climbstation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.databinding.ActivityClimbingBinding
import fi.metropolia.climbstation.databinding.ActivityClimbingHistoryBinding

class HistoryActivity: AppCompatActivity() {

    lateinit var  binding: ActivityClimbingHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}