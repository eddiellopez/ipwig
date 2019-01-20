package com.azazel.ipwig

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import java.net.Inet4Address
import java.net.Inet6Address

/**
 * Connection utils.
 */
class ConnectionUtils {
    companion object {

        private const val TAG = "ConnectionUtils"
        /**
         * Finds and returns the active connections.
         *
         * @param cm The connection manager.
         * @return A list of active connections.
         */
        fun findConnections(cm: ConnectivityManager): List<ConnectionInfo> {
            val connections: MutableList<ConnectionInfo> = mutableListOf()

            cm.allNetworks.asList()
                .filter { cm.getNetworkInfo(it).isConnected }
                .forEach {
                    val linkProperties = cm.getLinkProperties(it)
                    val networkCapabilities = cm.getNetworkCapabilities(it)

                    val hasData = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    val hasWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

                    // Find the addresses
                    var ipV4Address = ""
                    var ipV6Address = ""
                    linkProperties.linkAddresses.map { linkAddress -> linkAddress.address }
                        .filter { address -> !address.isLoopbackAddress && !address.isMulticastAddress }
                        .forEach { address ->
                            if (address is Inet4Address) {
                                ipV4Address = address.hostAddress
                            } else if (address is Inet6Address) {
                                ipV6Address = address.hostAddress
                            }
                        }

                    // Build the connection info object
                    val connectionInfo = ConnectionInfo(
                        linkProperties.interfaceName!!,
                        hasData, hasWifi, ipV4Address, ipV6Address
                    )
                    Log.i(TAG, connectionInfo.toString())
                    // Add the connection info to the list
                    connections.add(connectionInfo)
                }
            return connections
        }
    }
}