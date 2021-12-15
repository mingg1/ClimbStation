package fi.metropolia.climbstation.activities

import android.os.Bundle
import android.util.Log
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

class CreateProfileActivity : AppCompatActivity() {
    private var phaseNum = 1
    lateinit var binding: ActivityCustomTerrainProfileBinding
    private val terrainProfileViewModel: TerrainProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCustomTerrainProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textPhase.text = "Phase $phaseNum"
        val profileContainer = binding.profileContainer

        binding.buttonAddPhase.setOnClickListener {
            val phaseField =
                layoutInflater.inflate(R.layout.custom_profile_phase_field, null, false)
            phaseField.findViewById<TextView>(R.id.text_phase).text = "Phase ${++phaseNum}"
            phaseField.findViewById<TextView>(R.id.text_remove).setOnClickListener {
                phaseNum--
                profileContainer.removeView(phaseField) }
            profileContainer.addView(phaseField)
            Log.d("children", profileContainer.children.toList().toString())
        }

        binding.buttonSaveProfile.setOnClickListener {
            val profileName = binding.textProfileName.editText?.text.toString()
            val profilePhases: MutableList<Phase> = ArrayList()
            profileContainer.children.forEach {
                if (it.id == R.id.phase_field) {
                    val phase = Phase(
                        it.findViewById<TextInputLayout>(R.id.text_distance).editText?.text.toString().toInt(),
                        it.findViewById<TextInputLayout>(R.id.text_angle).editText?.text.toString().toInt()
                    )
                    profilePhases.add(phase)
                }
            }
            Log.d("children", profilePhases.toString())
            terrainProfileViewModel.addTerrainProfile(TerrainProfile(0,profileName,profilePhases,1))
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right)
    }

}