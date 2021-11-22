package fi.metropolia.climbstation.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.*
import fi.metropolia.climbstation.databinding.ActivityClimbingBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Constants.Companion.CLIMB_MODES
import fi.metropolia.climbstation.util.Constants.Companion.SERIAL_NUM
import fi.metropolia.climbstation.R
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingBinding
    private lateinit var viewModel: ClimbStationViewModel
    private val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NetworkMonitor(application).startNetworkCallback()

        viewModel = ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        binding.textSpeedValue.text = getString(R.string.speed, 0)

        val difficultyLevelTv = binding.listDifficulty
        val difficultyLevels = TerrainProfiles.difficultyLevels.map { it.name }
        val climbModeTv = binding.listClimbMode

        val difficultyLevelList = DropDownList(this, difficultyLevelTv, difficultyLevels)
        difficultyLevelList.setDropDownHeight(620)
        DropDownList(this, climbModeTv, CLIMB_MODES)

        binding.viewLayout.setOnClickListener { hideKeyboard(it) }
        binding.sliderSpeed.addOnChangeListener { _, value, _ ->
            hideKeyboard(binding.viewLayout)
            binding.textSpeedValue.text = getString(R.string.speed, value.toInt())
        }

        viewModel.logIn()
        viewModel.loginResponse.observe(this, { res ->
            if (res != null && res.body()?.response != "NOTOK") {
                Log.d("response1", res.body()?.response ?: "none")
                Log.d("response2", res.body()?.clientKey ?: "dd")
            } else {
                Log.d("response3", res?.errorBody().toString() ?: "dd")
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                makeAlert { viewModel.logIn() }

            }

        })

        binding.buttonStart.setOnClickListener {
            startOperation(difficultyLevelTv.text.toString(), climbModeTv.text.toString())
        }
    }

    private fun startOperation(difficultyLevel: String, climbMode: String) {
        val totalLength = binding.textLength.editText?.text.toString()
        val speedValue = binding.sliderSpeed.value.toInt().toString()
        val clientKey = viewModel.loginResponse.value?.body()?.clientKey
        val angle =
            TerrainProfiles.difficultyLevels.find { it.name == difficultyLevel }?.profiles?.get(0)?.angle.toString()

        if (NetworkVariables.isNetworkConnected && clientKey != null && checkFilledValues(
                totalLength,
                speedValue
            )
        ) {
            lifecycleScope.launch {
                async {
                    val speedReq = SpeedRequest(
                        SERIAL_NUM,
                        clientKey!!,
                        speedValue
                    )
                    val angleReq = AngleRequest(
                        SERIAL_NUM,
                        clientKey,
                        angle
                    )
                    val speedRes = repository.setSpeed(speedReq)
                    viewModel.speedResponse.value = speedRes
                    repository.setAngle(angleReq)
                    viewModel.angleResponse.observe(
                        this@MainActivity,
                        { res -> Log.d("angle", "$res" ?: "none") })

                    val operationReq = OperationRequest(SERIAL_NUM, clientKey, "start")
                    val operationRes = repository.setOperation(operationReq)
                    viewModel.operationResponse.value = operationRes

                }.await()
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
        } else if (clientKey == null) {
            viewModel.logIn()
            startOperation(difficultyLevel, climbMode)
        } else if (!NetworkVariables.isNetworkConnected) {
            makeAlert { startOperation(difficultyLevel, climbMode) }
        } else {
            Toast.makeText(
                this,
                "Make sure that you set valid speed and total length.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun makeAlert(function: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No internet connection")
        builder.setMessage("Do you want to try again?")
        builder.setNeutralButton("Cancel") { _, which ->
            Toast.makeText(applicationContext, "clicked cancel", Toast.LENGTH_LONG).show()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            function()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun checkFilledValues(totalLength: String, speed: String): Boolean =
        (totalLength != "" && totalLength != "0") && speed != "0.0"

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.menu_settings) {
            goToSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToSettingsActivity() {
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
    }
}
