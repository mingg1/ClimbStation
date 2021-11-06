package fi.metropolia.climbstation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import fi.metropolia.climbstation.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.text_speed_value).text = getString(R.string.speed,0)

        val items = listOf("Beginner", "Warm up", "Easy", "Power", "Athlete", "Pro athlete")
        val adapter = ArrayAdapter(this,R.layout.list_item, R.id.list_item, items)
       // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        findViewById<MaterialAutoCompleteTextView>(R.id.list_difficulty).setAdapter(adapter)
        findViewById<MaterialAutoCompleteTextView>(R.id.list_difficulty).setText(items.get(0),false);
        findViewById<Button>(R.id.button_start).setOnClickListener {
            val intent = Intent(this, ClimbingProgressActivity::class.java)
            startActivity(intent)
        }

            findViewById<Slider>(R.id.slider_speed).addOnChangeListener { slider, value, fromUser ->
                Log.d("test", value.toString())
                findViewById<TextView>(R.id.text_speed_value).text = getString(R.string.speed,value.toInt())
            }
    }
}