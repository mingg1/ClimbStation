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

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val actionBar = supportActionBar
        actionBar!!.title = "Settings Activity"
        actionBar.setDisplayHomeAsUpEnabled(true)
       findViewById<BottomNavigationView>(R.id.bottom_navigation).menu[2].isChecked = true

        val length = resources.getStringArray(R.array.length)
        val lengthAdapter = ArrayAdapter(this, R.layout.dropdown_item, length)

        val autocompleteLengthTV = findViewById<AutoCompleteTextView>(R.id.list_length)
        autocompleteLengthTV.setAdapter(lengthAdapter)

        val calorie = resources.getStringArray(R.array.calorie)
        val calorieAdapter = ArrayAdapter(this, R.layout.dropdown_item, calorie)

        val autocompleteCalorieTV = findViewById<AutoCompleteTextView>(R.id.list_calorie)
        autocompleteCalorieTV.setAdapter(calorieAdapter)

        findViewById<Button>(R.id.settingsOwnerLogin).setOnClickListener {
            val intent = Intent(this, OwnersLoginActivity::class.java)
            startActivity(intent)
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
