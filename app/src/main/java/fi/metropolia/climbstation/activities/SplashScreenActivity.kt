package fi.metropolia.climbstation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for splash screen
 *
 * @author Anne Pier Merkus
 */
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, QRCodeScannerActivity::class.java))
        finish()
    }
}