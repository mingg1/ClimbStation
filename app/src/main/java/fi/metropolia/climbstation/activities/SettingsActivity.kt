package fi.metropolia.climbstation.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.SettingsActivityBinding
import fi.metropolia.climbstation.ui.scaleAnimation

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.menu[2].isChecked = true

        val sf = getSharedPreferences("climbStation", MODE_PRIVATE)
        val length = resources.getStringArray(R.array.length)
        val lengthAdapter = ArrayAdapter(this, R.layout.dropdown_item, length)
        val lengthUnitList = binding.listLength
        lengthUnitList.setAdapter(lengthAdapter)
        lengthUnitList.setText(length[2], false)

        val calorie = resources.getStringArray(R.array.calorie)
        val calorieAdapter = ArrayAdapter(this, R.layout.dropdown_item, calorie)
        val calorieUnitList = binding.listCalorie
        calorieUnitList.setAdapter(calorieAdapter)
        calorieUnitList.setText(calorie[1], false)

        binding.settingsOwnerLogin.setOnClickListener {
            val intent = Intent(this, OwnersActivity::class.java)
            startActivity(intent)
        }

        binding.menuCreateProfile.setOnClickListener {
            makeTouchAnimation(it)
            startActivity(Intent(this, CreateProfileActivity::class.java))
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
        }

        binding.menuBodyWeight.setOnClickListener { inputDialog(sf) }
        binding.menuManageCustomProfile.setOnClickListener {
            makeTouchAnimation(it)
            startActivity(Intent(this, ManageProfileActivity::class.java))
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_climb -> {
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    finishAffinity()
                }
                R.id.menu_history -> {
                    startActivity(
                        Intent(
                            this,
                            HistoryActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    )
                    finishAffinity()
                }
            }
            true
        }

    }

    private fun inputDialog(sf: SharedPreferences) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Set your body weight")

        val input = EditText(this)
        input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.setText(sf.getInt("weight", 60).toString())
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ -> saveNewWeight(sf, input.text.toString()) }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun saveNewWeight(sf: SharedPreferences, weightText: String) {
        sf.edit().putInt("weight", weightText.toInt()).apply()
        Toast.makeText(this, "Your weight is saved!", Toast.LENGTH_LONG).show()
    }


    private fun makeTouchAnimation(view: View) {
        view.scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
        view.scaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 500)
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}