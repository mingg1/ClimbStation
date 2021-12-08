package fi.metropolia.climbstation.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.client.android.BeepManager

import com.journeyapps.barcodescanner.DefaultDecoderFactory

import com.google.zxing.BarcodeFormat

import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView

import com.google.zxing.ResultPoint

import com.journeyapps.barcodescanner.BarcodeResult

import com.journeyapps.barcodescanner.BarcodeCallback

import com.journeyapps.barcodescanner.DecoratedBarcodeView
import fi.metropolia.climbstation.R
import java.util.*


class QRCodeScannerActivity:AppCompatActivity() {
//    private lateinit var mQrResultLauncher : ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_qr_code_scanner)
//
//        mQrResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if(it.resultCode == Activity.RESULT_OK) {
//                val result = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
//
//                if(result.contents != null) {
//                    // Do something with the contents (this is usually a URL)
//                    println(result.contents)
//                }
//            }
//        }
//
//        //startScanner()
//    }
//
//    private fun startScanner() {
//        val scanner = IntentIntegrator(this)
//        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
//        scanner.setOrientationLocked(false)
//        mQrResultLauncher.launch(scanner.createScanIntent())
//    }
    private lateinit var barcodeView: DecoratedBarcodeView
    private var beepManager: BeepManager? = null
    private var lastText: String? = null

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }
            lastText = result.text
            barcodeView.setStatusText(result.text)
            beepManager!!.playBeepSoundAndVibrate()

            //Added preview of scanned barcode
//            val imageView: ImageView = findViewById(R.id.barcodePreview)
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)
        barcodeView = findViewById(R.id.barcode_scanner)
        val formats: Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.getBarcodeView().decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(intent)
        barcodeView.decodeContinuous(callback)
        beepManager = BeepManager(this)

        supportActionBar!!.hide()

        findViewById<Button>(R.id.button_skip).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    fun pause(view: View?) {
        barcodeView.pause()
    }

    fun resume(view: View?) {
        barcodeView.resume()
    }

    fun triggerScan(view: View?) {
        barcodeView.decodeSingle(callback)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}