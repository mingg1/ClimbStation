package fi.metropolia.climbstation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fi.metropolia.climbstation.activities.ClimbingProgressActivity
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityClimbingBinding
import fi.metropolia.climbstation.databinding.FragmentClimbProgramsBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Constants
import fi.metropolia.climbstation.util.Constants.Companion.CLIMB_MODES
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClimbSettingsFragment: Fragment() {
    private lateinit var binding: ActivityClimbingBinding
    private lateinit var climbStationViewModel: ClimbStationViewModel
    private val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)
    private lateinit var totalLength: String
    private lateinit var speedValue: String
    private var clientKey: String? = null
    private lateinit var angle: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        val profiles = terrainProfileViewModel.getTerrainProfiles()
        clientKey = requireActivity().getSharedPreferences("climbStation", AppCompatActivity.MODE_PRIVATE).getString("clientKey", "")

        binding = ActivityClimbingBinding.inflate(inflater, container, false)
        val view = binding.root

        val difficultyLevelTv = binding.listDifficulty
        val difficultyLevels = profiles.map { it.name }
        val difficultyLevelList = DropDownList(requireContext(), difficultyLevelTv, difficultyLevels)
        difficultyLevelList.setDropDownHeight(620)

        val climbModeTv = binding.listClimbMode
        DropDownList(requireContext(), climbModeTv, CLIMB_MODES)

        binding.textSpeedValue.text = getString(R.string.speed, 0)
        binding.viewLayout.setOnClickListener { hideKeyboard(it) }
        binding.sliderSpeed.addOnChangeListener { _, value, _ ->
            hideKeyboard(binding.viewLayout)
            binding.textSpeedValue.text = getString(R.string.speed, value.toInt())
        }

        binding.buttonStart.setOnClickListener {
            setValues(difficultyLevelTv.text.toString(), clientKey!!)
            startOperation(difficultyLevelTv.text.toString(), climbModeTv.text.toString())
        }

        return view
    }

    private fun setValues(difficultyLevel: String, clientKeyTxt: String) {
        totalLength = binding.textLength.editText?.text.toString()
        speedValue = (binding.sliderSpeed.value.toInt()).toString() // cm to mm
//        clientKey = requireActivity().getSharedPreferences("climbStation", AppCompatActivity.MODE_PRIVATE).getString("clientKey", "")
        angle =
            TerrainProfilesObject.terrainProfiles.find { it.name == difficultyLevel }?.profiles?.get(
                0
            )?.angle.toString()
    }

    private fun startOperation(difficultyLevel: String, climbMode: String) {
        if (!validValues(totalLength, speedValue)) return showInvalidValuesToast()
        if (!NetworkVariables.isNetworkConnected) return makeAlert {
            startOperation(
                difficultyLevel,
                climbMode
            )
        } else if (clientKey == null) {
            climbStationViewModel.logIn()
            startOperation(difficultyLevel, climbMode)
            return
        }

        lifecycleScope.launch {
            async {
                val speedReq = SpeedRequest(Constants.SERIAL_NUM, clientKey!!, speedValue)

                climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)

                val angleReq = AngleRequest(Constants.SERIAL_NUM, clientKey!!, angle)
                climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)

                val operationReq = OperationRequest(Constants.SERIAL_NUM, clientKey!!, "start")
                climbStationViewModel.operationResponse.value =
                    repository.setOperation(operationReq)

            }.await()

            startClimbingProgressActivity(difficultyLevel, climbMode)
        }

    }

    private fun startClimbingProgressActivity(difficultyLevel: String, climbMode: String) {
        val intent = Intent(requireContext(), ClimbingProgressActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("clientKey", clientKey)
        intent.putExtra("difficultyLevel", difficultyLevel)
        intent.putExtra("speed", speedValue)
        intent.putExtra("length", totalLength)
        intent.putExtra("mode", climbMode)

        startActivity(intent)
        finishAffinity(requireActivity())
    }

    private fun makeAlert(function: () -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("No internet connection")
            .setMessage("Do you want to try again?")
            .setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(requireContext().applicationContext, "clicked cancel", Toast.LENGTH_LONG).show()
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
            requireContext(),
            "Make sure that you set valid speed and total length.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}