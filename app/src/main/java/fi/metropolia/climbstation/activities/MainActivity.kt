package fi.metropolia.climbstation.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.NetworkMonitor
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.TerrainProfiles
import fi.metropolia.climbstation.databinding.ActivityClimbingBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Constants.Companion.SERIAL_NUM
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClimbingBinding
    lateinit var viewModel: ClimbStationViewModel
    val repository = ClimbStationRepository()
    val viewModelFactory = ClimbStationViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityClimbingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textSpeedValue.text = getString(R.string.speed, 0)

        val difficultyLevelTv = binding.listDifficulty
        val difficultyLevels = TerrainProfiles.difficultyLevels.map { it.name }
        val adapter = ArrayAdapter(this, R.layout.list_item, R.id.list_item, difficultyLevels)
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        difficultyLevelTv.setAdapter(adapter)
        difficultyLevelTv.setText(difficultyLevels[0], false)
        difficultyLevelTv.dropDownHeight = 620

        binding.viewLayout.setOnClickListener { hideKeyboard(it) }

        binding.sliderSpeed.addOnChangeListener { _, value, fromUser ->
            binding.textSpeedValue.text = getString(R.string.speed, value.toInt())
        }

       // NetworkMonitor(application).startNetworkCallback()

        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)

        viewModel.logIn()
        viewModel.loginResponse.observe(this, { res ->
            if (res != null && res.body()?.response != "NOTOK") {
                Log.d("response", res.body()?.response ?: "none")
                Log.d("response", res.body()?.clientKey ?: "dd")
            } else {
                Log.d("response", res?.errorBody().toString() ?: "dd")
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
                while(res.body()?.response == "NOTOK" && res.body()?.response == null){
                    viewModel.logIn()
                }
            }
        })

        binding.buttonStart.setOnClickListener {
            val totalLength = binding.textLength.editText?.text.toString()
            val speedValue = binding.sliderSpeed.value.toInt().toString()

            if (checkFilledValues(totalLength, speedValue)) {
                lifecycleScope.launch {
                    async {
                        val speedReq = SpeedRequest(
                            SERIAL_NUM,
                            viewModel.loginResponse.value!!.body()!!.clientKey,
                            speedValue
                        )
                        val angleReq = AngleRequest(
                            SERIAL_NUM,
                            viewModel.loginResponse.value!!.body()!!.clientKey,
                            "15"
                        )
                        val speedRes = repository.setSpeed(speedReq)
                        viewModel.speedResponse.value = speedRes
                        viewModel.speedResponse.observe(this@MainActivity, Observer { res ->
                            Log.d("speed", "$res" ?: "none")
                        })

                        repository.setAngle(angleReq)
                        viewModel.angleResponse.observe(
                            this@MainActivity,
                            { res -> Log.d("angle", "$res" ?: "none") })

                        val operationReq = OperationRequest(
                            SERIAL_NUM,
                            viewModel.loginResponse.value!!.body()!!.clientKey,
                            "start"
                        )
                        val operationRes = repository.setOperation(operationReq)
                        viewModel.operationResponse.value = operationRes
                        viewModel.operationResponse.observe(
                            this@MainActivity,
                            { res -> Log.d("operation", "$res" ?: "none") })
                    }.await()
                    val intent = Intent(this@MainActivity, ClimbingProgressActivity::class.java)
                    intent.putExtra("clientKey", viewModel.loginResponse.value!!.body()!!.clientKey)
                    intent.putExtra("mode", difficultyLevelTv.text.toString())
                    intent.putExtra("speed", speedValue)
                    intent.putExtra("length", totalLength)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(
                    this,
                    "Make sure that you set valid speed and total length.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun checkFilledValues(totalLength: String, speed: String): Boolean =
        (totalLength != "" && totalLength != "0") && speed != "0.0"

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
