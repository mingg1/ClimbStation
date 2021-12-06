package fi.metropolia.climbstation.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.*
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingBinding
import fi.metropolia.climbstation.databinding.ActivityShowBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Constants.Companion.CLIMB_MODES
import fi.metropolia.climbstation.util.Constants.Companion.SERIAL_NUM
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowBinding
    private lateinit var climbStationViewModel: ClimbStationViewModel
    private val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
    private val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)

    private lateinit var totalLength: String
    private lateinit var speedValue: String
    private var clientKey: String? = null
    private lateinit var angle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkMonitor(application).startNetworkCallback()

        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
//        terrainProfileViewModel = ViewModelProvider(this)[TerrainProfileViewModel::class.java]


//        val difficultyLevelTv = binding.listDifficulty

        var profiles = terrainProfileViewModel.getTerrainProfiles()
        if (profiles.isEmpty()) {
            TerrainProfilesObject.terrainProfiles.forEach {
                val level = mutableListOf<Pair<Int, Int>>()
                it.profiles.forEach {
                    level.add(Pair(it.distance, it.angle))
                }
                terrainProfileViewModel.addTerrainProfile(
                    TerrainProfile(
                        0,
                        it.name,
                        it.profiles
                    )
                )
            }
        }
//        profiles = terrainProfileViewModel.getTerrainProfiles()

//            val difficultyLevels = profiles.map { it.name }
//            val difficultyLevelList = DropDownList(this, difficultyLevelTv, difficultyLevels)
//            difficultyLevelList.setDropDownHeight(620)

//        }

//        binding.textSpeedValue.text = getString(R.string.speed, 0)


//        val difficultyLevels = TerrainProfilesObject.terrainProfiles.map { it.name }
//        val climbModeTv = binding.listClimbMode
//
//
//        DropDownList(this, climbModeTv, CLIMB_MODES)
//
        val sf = getSharedPreferences("climbStation", MODE_PRIVATE)
        val clientKeyTxt = sf.getString("clientKey", "")

//        binding.viewLayout.setOnClickListener { hideKeyboard(it) }
//        binding.sliderSpeed.addOnChangeListener { _, value, _ ->
//            hideKeyboard(binding.viewLayout)
//            binding.textSpeedValue.text = getString(R.string.speed, value.toInt())
//        }
//
        if (clientKeyTxt == "") {
            climbStationViewModel.logIn()
            climbStationViewModel.loginResponse.observe(this, { res ->
                if (res != null && res.body()?.response != "NOTOK") {
                    Log.d("response1", res.body()?.response ?: "none")
                    Log.d("response2", res.body()?.clientKey ?: "dd")
                    val editor = sf.edit()
                    editor.putString("clientKey", res.body()?.clientKey)
                    editor.apply()
                } else {
                    Log.d("response3", res?.errorBody().toString() ?: "dd")
                    Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                    makeAlert { climbStationViewModel.logIn() }
                }
            })
        }
//
//        binding.buttonStart.setOnClickListener {
//            setValues(difficultyLevelTv.text.toString(), clientKeyTxt!!)
//            startOperation(difficultyLevelTv.text.toString(), climbModeTv.text.toString())
//        }

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
//
//    override fun onPause() {
//        super.onPause()
//        lifecycleScope.launch {
//            async{
//                val logOutReq = LogOutRequest(SERIAL_NUM, clientKey!!)
//                repository.logOut(logOutReq)
//            }.await()
//            val sharedPrefs: SharedPreferences = getSharedPreferences("climbStation", MODE_PRIVATE)
//            val editor = sharedPrefs.edit()
//            editor.clear()
//            editor.apply()
//        }
//    }

//    private fun setValues(difficultyLevel: String, clientKeyTxt: String) {
//        totalLength = binding.textLength.editText?.text.toString()
//        speedValue = (binding.sliderSpeed.value.toInt()).toString() // cm to mm
//        clientKey = getSharedPreferences("climbStation", MODE_PRIVATE).getString("clientKey", "")
//        angle =
//            TerrainProfilesObject.terrainProfiles.find { it.name == difficultyLevel }?.profiles?.get(
//                0
//            )?.angle.toString()
//    }
//
//    private fun startOperation(difficultyLevel: String, climbMode: String) {
//        if (!validValues(totalLength, speedValue)) return showInvalidValuesToast()
//        if (!NetworkVariables.isNetworkConnected) return makeAlert {
//            startOperation(
//                difficultyLevel,
//                climbMode
//            )
//        } else if (clientKey == null) {
//            climbStationViewModel.logIn()
//            startOperation(difficultyLevel, climbMode)
//            return
//        }
//
//        lifecycleScope.launch {
//            async {
//                val speedReq = SpeedRequest(SERIAL_NUM, clientKey!!, speedValue)
//
//                climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)
//
//                val angleReq = AngleRequest(SERIAL_NUM, clientKey!!, angle)
//                climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)
//
//                val operationReq = OperationRequest(SERIAL_NUM, clientKey!!, "start")
//                climbStationViewModel.operationResponse.value =
//                    repository.setOperation(operationReq)
//
//            }.await()
//
//            startClimbingProgressActivity(difficultyLevel, climbMode)
//        }
//
//    }

    private fun startClimbingProgressActivity(difficultyLevel: String, climbMode: String) {
        val intent = Intent(this@MainActivity, ClimbingProgressActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("clientKey", clientKey)
        intent.putExtra("difficultyLevel", difficultyLevel)
        intent.putExtra("speed", speedValue)
        intent.putExtra("length", totalLength)
        intent.putExtra("mode", climbMode)

        startActivity(intent)
        finishAffinity()
    }

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

    private fun validValues(totalLength: String, speed: String): Boolean =
        (totalLength != "" && totalLength != "0") && speed != "0.0"

    private fun showInvalidValuesToast() {
        Toast.makeText(
            this,
            "Make sure that you set valid speed and total length.",
            Toast.LENGTH_LONG
        ).show()
    }

//    private fun hideKeyboard(view: View) {
//        val inputMethodManager: InputMethodManager =
//            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}
