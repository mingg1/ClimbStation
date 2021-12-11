package fi.metropolia.climbstation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
//        Handler(Looper.getMainLooper()).postDelayed(
//            {
//                val intent = Intent(this@SplashScreenActivity, QRCodeScannerActivity::class.java)
//                startActivity(intent)
//                finish()
//            }, 5000
//        )
        startActivity(Intent(this@SplashActivity, QRCodeScannerActivity::class.java))
        finish()
    }
}