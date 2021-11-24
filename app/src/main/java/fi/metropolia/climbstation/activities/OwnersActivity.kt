package fi.metropolia.climbstation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fi.metropolia.climbstation.R

class OwnersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owners)

        val actionBar = supportActionBar
        actionBar!!.title = "Owner's Activity"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}