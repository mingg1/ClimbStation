package fi.metropolia.climbstation.activities

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import fi.metropolia.climbstation.Phase
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.viewModels.TerrainProfileViewModel
import fi.metropolia.climbstation.databinding.ActivityCustomTerrainProfileBinding

class ModifyProfileActivity:AppCompatActivity() {
    lateinit var binding: ActivityCustomTerrainProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCustomTerrainProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewTitle.text = "Modify terrain profile"
val profileId = intent.extras?.getLong("profileId")
        val terrainProfileViewModel: TerrainProfileViewModel by viewModels()
        val profile = terrainProfileViewModel.getTerrainProfileById(profileId!!.toInt())

        binding.textProfileName.editText?.setText(profile.name)

        var phaseNum = profile.phases.size
        if (profile.phases.size >1){
            profile.phases.forEach {
                if(profile.phases.indexOf(it)==0){
                    binding.textDistance.editText?.setText(profile.phases[0].distance.toString())
                    binding.textAngle.editText?.setText(profile.phases[0].angle.toString())
                }else
                {
                    val phaseField =
                        layoutInflater.inflate(R.layout.custom_profile_phase_field, null, false)
                    phaseField.findViewById<TextInputLayout>(R.id.text_angle).editText?.setText(
                        it.angle.toString()
                    )
                    phaseField.findViewById<TextInputLayout>(R.id.text_distance).editText?.setText(
                        it.distance.toString()
                    )
                    phaseField.findViewById<TextView>(R.id.text_phase).text = "Phase ${profile.phases.indexOf(it)+1}"
                    phaseField.findViewById<TextView>(R.id.text_remove).setOnClickListener {
//                        index--
                        binding.profileContainer.removeView(phaseField)
                    }
                    binding.profileContainer.addView(phaseField)
                }
            }
        }

        binding.buttonAddPhase.setOnClickListener {
            val phaseField =
                layoutInflater.inflate(R.layout.custom_profile_phase_field, null, false)
            phaseField.findViewById<TextView>(R.id.text_phase).text = "Phase ${++phaseNum}"
            phaseField.findViewById<TextView>(R.id.text_remove).setOnClickListener {
                phaseNum--
                binding.profileContainer.removeView(phaseField) }
            binding.profileContainer.addView(phaseField)
        }

        binding.buttonSaveProfile.text = "Modify profile"
        binding.buttonSaveProfile.setOnClickListener {
            val profileName = binding.textProfileName.editText?.text.toString()
            val profilePhases: MutableList<Phase> = ArrayList()
            binding.profileContainer.children.forEach {
                if (it.id == R.id.phase_field) {
                    val phase = Phase(
                        it.findViewById<TextInputLayout>(R.id.text_distance).editText?.text.toString().toInt(),
                        it.findViewById<TextInputLayout>(R.id.text_angle).editText?.text.toString().toInt()
                    )
                    profilePhases.add(phase)
                }
            }
            terrainProfileViewModel.updateTerrainProfile(TerrainProfile(profile.id,profileName,profilePhases,profile.custom))
            onBackPressed()
        }
    }
}