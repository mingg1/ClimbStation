package fi.metropolia.climbstation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import fi.metropolia.climbstation.activities.ClimbingProgressActivity
import fi.metropolia.climbstation.activities.MainActivity
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.FragmentClimbProgramsBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.util.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

var visibility = false

class ClimbProgramFragment : Fragment(), RecyclerviewClickListener {

    private lateinit var binding: FragmentClimbProgramsBinding
    private lateinit var climbStationViewModel: ClimbStationViewModel
    private val repository = ClimbStationRepository()
    private val viewModelFactory = ClimbStationViewModelFactory(repository)
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        val climbPrograms = terrainProfileViewModel.getTerrainProfiles()

        val sf =
            requireActivity().getSharedPreferences("climbStation", AppCompatActivity.MODE_PRIVATE)
        val clientKey = sf.getString("clientKey", "")

        binding = FragmentClimbProgramsBinding.inflate(inflater, container, false)
        val view = binding.root
        val info = binding.programInfoContainer

        view.setOnClickListener { Transition(info, view).hideInfo() }

        val programList = binding.programList
        programList.layoutManager = GridLayoutManager(requireContext(), 2)
        programList.adapter = ProgramListAdapter(climbPrograms, requireContext(), view, this)
        programList.setOnScrollChangeListener { _, _, _, _, _ ->
            if (visibility) Transition(
                info,
                view
            ).hideInfo()
        }
        programList.setOnTouchListener { _, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Transition(info, view).hideInfo()
                    true
                }
                else -> false
            }
        }

        val startBtn = binding.buttonStartClimbing
        startBtn.setOnClickListener {
            val selectedLevel = climbPrograms[index]
            lifecycleScope.launch {
                async {
                    // The average speed is 6 mm/sec
                    val speedReq = SpeedRequest(Constants.SERIAL_NUM, clientKey!!, "6")

                    climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)

                    val angleReq = AngleRequest(
                        Constants.SERIAL_NUM,
                        clientKey,
                        selectedLevel.phases[0].angle.toString()
                    )
                    climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)

                    val operationReq = OperationRequest(Constants.SERIAL_NUM, clientKey, "start")
                    climbStationViewModel.operationResponse.value =
                        repository.setOperation(operationReq)

                }.await()

                var totalLength = 0
                selectedLevel.phases.forEach { totalLength += it.distance }

                val intent = Intent(context, ClimbingProgressActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                intent.putExtra("clientKey", clientKey)
                intent.putExtra("difficultyLevel", selectedLevel.name)
                intent.putExtra("speed", "6")
                intent.putExtra("length", totalLength.toString())
                intent.putExtra("mode", "Looping")
                Log.d("level", "${selectedLevel.name},${totalLength}")
                startActivity(intent)
                ActivityCompat.finishAffinity(context as MainActivity)
            }
        }

        return view
    }

    override fun recyclerViewClickListener(position: Int) {
        index = position
    }

}