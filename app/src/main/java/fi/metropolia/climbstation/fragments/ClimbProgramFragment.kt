package fi.metropolia.climbstation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import fi.metropolia.climbstation.*
import fi.metropolia.climbstation.activities.ClimbingProgressActivity
import fi.metropolia.climbstation.activities.MainActivity
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.FragmentClimbProgramsBinding
import fi.metropolia.climbstation.network.*
import fi.metropolia.climbstation.ui.Transition
import fi.metropolia.climbstation.ui.feedBackTouchListener
import fi.metropolia.climbstation.util.Config
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Fragment for showing climbing programs
 *
 * @author Minji Choi
 *
 */
class ClimbProgramFragment : Fragment(), RecyclerviewClickListener {

    private lateinit var binding: FragmentClimbProgramsBinding
    private lateinit var climbStationViewModel: ClimbStationViewModel
    private var selectedProgramId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val repository = ClimbStationRepository(Config(requireContext()).baseUrl!!)
       val viewModelFactory = ClimbStationViewModelFactory(repository)
        climbStationViewModel =
            ViewModelProvider(this, viewModelFactory)[ClimbStationViewModel::class.java]
        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        val basicClimbPrograms = terrainProfileViewModel.getBaseTerrainProfiles()

        val sf =
            requireActivity().getSharedPreferences("climbStation", AppCompatActivity.MODE_PRIVATE)
        val clientKey = sf.getString("clientKey", "")
        val serialNumber = sf.getString("serialNumber", "")

        binding = FragmentClimbProgramsBinding.inflate(inflater, container, false)
        val view = binding.root
        val info = binding.programInfoContainer

        view.setOnClickListener { Transition(info, view).hideElement() }

        val programList = binding.programList
        programList.layoutManager = GridLayoutManager(requireContext(), 2)

        terrainProfileViewModel.getCustomTerrainProfiles().observe(this, {
            val allPrograms = it + basicClimbPrograms
            programList.adapter = ProgramListAdapter(allPrograms, requireContext(), view, this)

            // filtering climbing programs (custom, predefined, or both)
            binding.spinnerPrograms.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?, itemView: View?, position: Int, id: Long
                    ) {
                        val selectedItem = adapterView?.getItemAtPosition(position).toString()
                        val filterModes = resources.getStringArray(R.array.filter_modes)
                        when(selectedItem){
                           filterModes[0]-> programList.adapter = ProgramListAdapter(allPrograms, requireContext(), view, this@ClimbProgramFragment)
                            filterModes[1]-> programList.adapter = ProgramListAdapter(it, requireContext(), view, this@ClimbProgramFragment)
                            filterModes[2]-> programList.adapter = ProgramListAdapter(basicClimbPrograms, requireContext(), view, this@ClimbProgramFragment)
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        })

        programList.setOnScrollChangeListener { _, _, _, _, _ ->
            if (binding.programInfoContainer.visibility == View.VISIBLE) Transition(info, view).hideElement()
           }
        programList.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Transition(info, view).hideElement()
                    true
                }
                else -> false
            }
        }

        val startBtn = binding.buttonStartClimbing
        startBtn.feedBackTouchListener()
        startBtn.setOnClickListener {
            // start selected program with the average speed
            val selectedLevel = terrainProfileViewModel.getTerrainProfileById(selectedProgramId)
            lifecycleScope.launch {
                async {
                    // The average speed is 6 mm/sec
                    val speedReq = SpeedRequest(serialNumber!!, clientKey!!, "6")

                    climbStationViewModel.speedResponse.value = repository.setSpeed(speedReq)

                    val angleReq = AngleRequest(
                        serialNumber, clientKey, selectedLevel.phases[0].angle.toString()
                    )
                    climbStationViewModel.angleResponse.value = repository.setAngle(angleReq)

                    val operationReq = OperationRequest(serialNumber, clientKey, "start")
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
                startActivity(intent)
                ActivityCompat.finishAffinity(context as MainActivity)
            }
        }

        return view
    }

    override fun recyclerViewClickListener(programId: Long) {
        selectedProgramId = programId
    }

}