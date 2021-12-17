package fi.metropolia.climbstation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R

/**
 * Activity for login to settings of the owner
 *
 * @author Anjan Shakya
 *
 */

class OwnersActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owners_activity)

    }
}