package fi.metropolia.climbstation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.databinding.SettingsActivityBinding
import fi.metropolia.climbstation.feedBackTouchListener
import fi.metropolia.climbstation.scaleAnimation

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.menu[2].isChecked = true

        val length = resources.getStringArray(R.array.length)
        val lengthAdapter = ArrayAdapter(this, R.layout.dropdown_item, length)

        binding.listLength.setAdapter(lengthAdapter)

        val calorie = resources.getStringArray(R.array.calorie)
        val calorieAdapter = ArrayAdapter(this, R.layout.dropdown_item, calorie)

        val autocompleteCalorieTV = findViewById<AutoCompleteTextView>(R.id.list_calorie)
        binding.listCalorie.setAdapter(calorieAdapter)

        binding.settingsOwnerLogin.setOnClickListener {
            val intent = Intent(this, OwnersActivity::class.java)
            startActivity(intent)
        }


        binding.menuCreateProfile.setOnClickListener {
            it.scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
            it.scaleAnimation(0.95f, 1.0f,0.95f, 1.0f, 500)
            startActivity(
                Intent(
                    this,
                    CreateProfileActivity::class.java
                )
            )
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
        }


        binding.menuManageCustomProfile.setOnClickListener {
            it.scaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 100)
            it.scaleAnimation(0.95f, 1.0f,0.95f, 1.0f, 500)
            startActivity(
                Intent(
                    this,
                    ManageProfileActivity::class.java
                )
            )
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
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

    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}


//        findViewById<MaterialAutoCompleteTextView>(R.id.list_length).setAdapter(lengthAdapter)
//        findViewById<MaterialAutoCompleteTextView>(R.id.list_length).setText(
//            length[0], false)
//
//        val calorie = listOf("kilogram(kg)", "grams(gm)", "pounds(lbs)")
//        val calorieAdapter = ArrayAdapter(this, R.layout.dropdown_item, calorie)
//        findViewById<MaterialAutoCompleteTextView>(R.id.list_calorie).setAdapter(calorieAdapter)
//        findViewById<MaterialAutoCompleteTextView>(R.id.list_calorie).setText(
//            calorie[0], false)


//        setContentView(R.layout.settings_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.settings, SettingsFragment())
//                .commit()
//        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }

//    class SettingsFragment : PreferenceFragmentCompat() {
//        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey)
//        }
