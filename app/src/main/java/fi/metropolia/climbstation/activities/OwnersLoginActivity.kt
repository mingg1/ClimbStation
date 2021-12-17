package fi.metropolia.climbstation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fi.metropolia.climbstation.R

/**
 * Activity for login to settings of the owner
 *
 * @author Anjan Shakya
 *
 */
class OwnersLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owners_login)

        val actionBar = supportActionBar
        actionBar!!.title = "Owner's Activity"
        actionBar.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val intent = Intent(this, OwnersActivity::class.java)
            startActivity(intent)
        }
    }
}