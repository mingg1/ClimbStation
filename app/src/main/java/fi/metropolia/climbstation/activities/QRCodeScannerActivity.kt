package fi.metropolia.climbstation.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import fi.metropolia.climbstation.R
import fi.metropolia.climbstation.util.Config
import fi.metropolia.climbstation.util.Constants

class QRCodeScannerActivity : AppCompatActivity() {
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
            handleSerialNumberResult(result.text)
            beepManager!!.playBeepSoundAndVibrate()

            //Added preview of scanned barcode
//            val imageView: ImageView = findViewById(R.id.barcodePreview)
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // do nothing
                } else {
                    inputDialog()
                }
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)
        supportActionBar!!.hide()
Log.d("config",Config(this).mainVariables.toString())
        // check if a serial number has been saved in the app
        val sf= getSharedPreferences("climbStation", MODE_PRIVATE)
        val serialNumber = sf.getString("serialNumber", "")
        if (serialNumber != null && serialNumber != "") {
            moveToNextActivity()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
                }
            }

            barcodeView = findViewById(R.id.barcode_scanner)
            val formats: Collection<BarcodeFormat> =
                listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
            barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
            barcodeView.initializeFromIntent(intent)
            barcodeView.decodeContinuous(callback)
            beepManager = BeepManager(this)

            findViewById<Button>(R.id.button).setOnClickListener {
                inputDialog()
            }
        }

    }

    private fun inputDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Serial Number")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ -> handleSerialNumberResult(input.text.toString()) }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun handleSerialNumberResult(serialNumber: String) {
        val sf= getSharedPreferences("climbStation", MODE_PRIVATE)
        sf.edit().putString("serialNumber", serialNumber)?.apply()
        moveToNextActivity()
    }

    private fun moveToNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}