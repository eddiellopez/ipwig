package com.azazel.ipwig.data

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import java.net.Inet4Address
import java.net.Inet6Address

/**
 * A class that models what's relevant for this app regarding to network connections.
 */
data class ConnectionInfo(
    val name: String,
    val hasData: Boolean,
    val hasWifi: Boolean,
    val ipv4: String?,
    val ipv6: String?
) {

    override fun toString(): String {
        return "ConnectionInfo(name='$name', hasData=$hasData, " +
                "hasWifi=$hasWifi, ipv4='$ipv4', ipv6='$ipv6')"
    }
}

fun networkToConnectionInfo(
    cm: ConnectivityManager,
    network: Network?
): ConnectionInfo {
    // This two better not be null
    val linkProperties = cm.getLinkProperties(network)!!
    val networkCapabilities = cm.getNetworkCapabilities(network)!!

    // Find the addresses
    val associate = linkProperties.linkAddresses
        .map { linkAddress -> linkAddress.address }
        .filter { address -> !address.isLoopbackAddress && !address.isMulticastAddress }
        .associate { netAddress -> Pair(netAddress::class.java, netAddress.hostAddress) }


    // Build the connection info object
    return ConnectionInfo(
        linkProperties.interfaceName!!,
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
        associate[Inet4Address::class.java],
        associate[Inet6Address::class.java]
    )
}