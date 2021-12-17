package fi.metropolia.climbstation.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.viewModels.ClimbHistoryViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingHistoryBinding

/**
 * Activity to display climbing histories
 *
 * @author Minji Choi
 *
 */
class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityClimbingHistoryBinding
    private lateinit var climbHistoryViewModel: ClimbHistoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        climbHistoryViewModel = ViewModelProvider(this)[ClimbHistoryViewModel::class.java]
        climbHistoryViewModel.getClimbHistoryHistory.observe(this, {
            val historyRecyclerView = binding.listView
            historyRecyclerView.layoutManager = LinearLayoutManager(this)
            historyRecyclerView.adapter = HistoryListAdapter(it.reversed(),this)
        })
        binding.bottomNav.menu[1].isChecked = true

        binding.bottomNav.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_climb -> {
                    startActivity(
                        Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    finishAffinity()
                }
                R.id.menu_settings -> {
                    startActivity(
                        Intent(this, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    finishAffinity()
                }
            }
            true
        }
    }
    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_climb -> {
                startActivity(
                    Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
            }
            R.id.menu_settings -> startActivity(
                Intent(this, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
        return super.onOptionsItemSelected(item)
    }

}