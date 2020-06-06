package com.azazel.ipwig.data

import android.content.Context
import android.net.ConnectivityManager

/**
 * The provider of local connection information.
 */
class ConnectionInfoProvider {
    /**
     * Finds and returns all the available connections (that are currently connected).
     *
     * @param context The calling context.
     * @return The list of connections.
     */
    fun findConnections(context: Context): List<ConnectionInfo> {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.allNetworks
            .map {
                networkToConnectionInfo(cm, it)
            }
    }
}
