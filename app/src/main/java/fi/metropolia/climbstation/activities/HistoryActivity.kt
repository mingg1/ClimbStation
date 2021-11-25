package fi.metropolia.climbstation.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.viewModels.ClimbViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingHistoryBinding

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityClimbingHistoryBinding
lateinit var climbViewModel: ClimbViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        climbViewModel = ViewModelProvider(this).get(ClimbViewModel::class.java)
        climbViewModel.getClimbHistory.observe(this,{
            val historyRecyclerView = binding.historyList
            historyRecyclerView.layoutManager =LinearLayoutManager(this)
            historyRecyclerView.adapter = HistoryListAdapter(it)
        })

        binding.bottomNav.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_climb -> startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                R.id.menu_settings -> startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}