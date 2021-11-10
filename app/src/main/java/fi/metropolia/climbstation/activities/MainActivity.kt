package fi.metropolia.climbstation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fi.metropolia.climbstation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import network.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_climbing)
        findViewById<TextView>(R.id.text_speed_value).text = getString(R.string.speed, 0)

        val items = listOf("Beginner", "Warm up", "Easy", "Power", "Athlete", "Pro athlete")
        val adapter = ArrayAdapter(this, R.layout.list_item, R.id.list_item, items)
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        findViewById<MaterialAutoCompleteTextView>(R.id.list_difficulty).setAdapter(adapter)
        findViewById<MaterialAutoCompleteTextView>(R.id.list_difficulty).setText(
            items.get(0),
            false
        );
        findViewById<Button>(R.id.button_start).setOnClickListener {
            val intent = Intent(this, ClimbingProgressActivity::class.java)
            startActivity(intent)
        }

        findViewById<Slider>(R.id.slider_speed).addOnChangeListener { slider, value, fromUser ->
            Log.d("test", value.toString())
            findViewById<TextView>(R.id.text_speed_value).text =
                getString(R.string.speed, value.toInt())
        }
        lateinit var viewModel: ClimbStationViewModel
        val repository = ClimbStationRepository()
        val viewModelFactory = ClimbStationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClimbStationViewModel::class.java)
        viewModel.logIn()
        viewModel.loginResponse.observe(this, Observer { res ->
            Log.d("response", res.response ?: "none")
            Log.d("response", res.clientKey ?: "dd")
        })

        findViewById<Button>(R.id.button_start).setOnClickListener {
            val totalLength = findViewById<TextInputLayout>(R.id.text_length).editText?.text
            val speedValue = findViewById<Slider>(R.id.slider_speed).value.toString()

            lifecycleScope.launch {
               async {
                    val speedReq = SpeedRequest(
                        "20110001",
                        viewModel.loginResponse.value!!.clientKey,
                        speedValue
                    )
                    val angleReq = AngleRequest(
                        "20110001",
                        viewModel.loginResponse.value!!.clientKey,
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
                        "20110001",
                        viewModel.loginResponse.value!!.clientKey,
                        "start"
                    )
                    val operationRes = repository.setOperation(operationReq)
                    viewModel.operationResponse.value = operationRes
                    viewModel.operationResponse.observe(
                        this@MainActivity,
                        { res -> Log.d("operation", "$res" ?: "none") })
                }.await()
                val intent = Intent(this@MainActivity, ClimbingProgressActivity::class.java)
                intent.putExtra("clientKey", viewModel.loginResponse.value!!.clientKey)
                startActivity(intent)
            }
//                viewModel.setSpeed(
//                    "20110001",
//                    viewModel.loginResponse.value!!.clientKey,
//                    speedValue
//                )
//
//                viewModel.speedResponse.observe(this@MainActivity, Observer { res ->
//                    Log.d("speed", "$res" ?: "none")
//                })
//                viewModel.setAngle("20110001", viewModel.loginResponse.value!!.clientKey, "15")
//                viewModel.angleResponse.observe(
//                    this@MainActivity,
//                    { res -> Log.d("angle", "$res" ?: "none") })


//                viewModel.setOperation(
//                    "20110001",
//                    viewModel.loginResponse.value!!.clientKey,
//                    "start"
//                )
//                viewModel.operationResponse.observe(
//                    this@MainActivity,
//                    { res -> Log.d("operation", "$res" ?: "none") })
//


            }

        }

    }
