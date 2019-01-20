package com.azazel.ipwig

/**
 * A class that models what's relevant for this app regarding to network connections.
 */
data class ConnectionInfo(
    val name: String, val hasData: Boolean,
    val hasWifi: Boolean, val ipv4: String = "", val ipv6: String
) {

    override fun toString(): String {
        return "ConnectionInfo(name='$name', hasData=$hasData, " +
                "hasWifi=$hasWifi, ipv4='$ipv4', ipv6='$ipv6')"
    }
}


