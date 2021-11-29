package fi.metropolia.climbstation.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.ActivityCustomTerrainProfileBinding

class CreateProfileActivity : AppCompatActivity() {
    private var phaseNum = 1
    lateinit var binding: ActivityCustomTerrainProfileBinding
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
            profileContainer.addView(phaseField)
            phaseField.findViewById<TextView>(R.id.text_phase).text = "Phase ${++phaseNum}"
            Log.d("children", profileContainer.children.toList().toString())
        }

        binding.buttonSaveProfile.setOnClickListener {
            val profilePhases: MutableList<Pair<Int, Int>> = ArrayList()
            profileContainer.children.forEach {
                if (it.id == R.id.phase_field) {
                    val phase = Pair(
                        it.findViewById<TextInputLayout>(R.id.text_distance).editText?.text.toString().toInt(),
                        it.findViewById<TextInputLayout>(R.id.text_angle).editText?.text.toString().toInt()
                    )
                    profilePhases.add(phase)
                }
            }
            Log.d("children", profilePhases.toString())
        }
    }
}