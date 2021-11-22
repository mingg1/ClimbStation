package fi.metropolia.climbstation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.ActivityClimbResultBinding

class ClimbingResultsActivity: AppCompatActivity() {
    private lateinit var binding:ActivityClimbResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbResultBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

    }
}