package fi.metropolia.climbstation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlin.properties.Delegates

/**
 * Class for keeping track of network state
 */
class NetworkMonitor
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
constructor(private val application: Application) {

    fun startNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerDefaultNetworkCallback(networkCallback)
    }

    fun stopNetworkCallback() {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            NetworkVariables.isNetworkConnected = true
        }

        override fun onLost(network: Network) {
            NetworkVariables.isNetworkConnected = false
        }

        override fun onUnavailable() {
            NetworkVariables.isNetworkConnected = false
        }
    }
}

object NetworkVariables{
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.d("networktets", "new value: $newValue")
    }
}