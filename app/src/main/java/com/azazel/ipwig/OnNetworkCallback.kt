package com.azazel.ipwig

import android.content.Context
import android.net.*
import android.util.Log

/**
 * Network callback subclass to act upon receiving network changes.
 *
 * @param context The calling context.
 */
class OnNetworkCallback(private val context: Context) : ConnectivityManager.NetworkCallback() {

    companion object {
        const val TAG = "OnNetworkCallback"
    }

    val request: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    override fun onLost(network: Network?) {
        Log.i(TAG, "onLost()")
        updateWidget()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        super.onLost(network)
    }

    override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
        Log.i(
            TAG,
            "onLinkPropertiesChanged()"
        )
        updateWidget()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        super.onLinkPropertiesChanged(network, linkProperties)
    }

    override fun onAvailable(network: Network?) {
        updateWidget()
        Log.i(
            TAG,
            "onAvailable()"
        )
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        super.onAvailable(network)
    }

    private fun updateWidget() {
        IpWidgetProvider.updateView(context)
    }
}