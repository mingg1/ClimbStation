package fi.metropolia.climbstation.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fi.metropolia.climbstation.*
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityShowBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Config
import fi.metropolia.climbstation.util.TerrainProfilesObject

/**
 * Activity to get the serial number and log in to the application
 *
 * @author Minji Choi
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowBinding
    private lateinit var climbStationViewModel: ClimbStationViewModel
    private val terrainProfileViewModel: TerrainProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkMonitor(application).startNetworkCallback()
        val configReader = Config(this)
        val repository = ClimbStationRepository(configReader.baseUrl.toString())
        val viewModelFactory = ClimbStationViewModelFactory(repository)
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        val sf = getSharedPreferences("climbStation", MODE_PRIVATE)
        val editor = sf.edit()
        val serialNumber = sf.getString("serialNumber", "")
        val clientKeyTxt = sf.getString("clientKey", "")

        editor.putInt("weight", 60)
        editor.apply()
        val profiles = terrainProfileViewModel.getTerrainProfiles()
        if (profiles.isEmpty()) {
            TerrainProfilesObject.terrainProfiles.forEach { it ->
                val level = mutableListOf<Pair<Int, Int>>()
                it.profiles.forEach { level.add(Pair(it.distance, it.angle)) }
                terrainProfileViewModel.addTerrainProfile(
                    TerrainProfile(0, it.name, it.profiles, 0)
                )
            }
        }

        if (clientKeyTxt == "") {
            climbStationViewModel.logIn(serialNumber!!,configReader.id!!,configReader.password!!)
            climbStationViewModel.loginResponse.observe(this, { res ->
                if (res != null && res.body()?.response != "NOTOK") {
                    editor.putString("clientKey", res.body()?.clientKey)
                    editor.apply()
                } else {
                    Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                    makeAlert { climbStationViewModel.logIn(serialNumber,configReader.id,configReader.password) }
                }
            })
        }

        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_history -> {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            HistoryActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                    )
                    finishAffinity()
                }
                R.id.menu_settings -> {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            SettingsActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    finishAffinity()
                }
            }
            true
        }

    }

    // make an alert when disconnected to network
    private fun makeAlert(function: () -> Unit) {
        val builder = AlertDialog.Builder(this)
            .setTitle("No internet connection")
            .setMessage("Do you want to try again?")
            .setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "clicked cancel", Toast.LENGTH_LONG).show()
            }
            .setPositiveButton("Yes") { _, _ ->
                function()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}
