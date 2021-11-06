package fi.metropolia.climbstation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fi.metropolia.climbstation.R

class QRCodeScannerActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)
    }
}