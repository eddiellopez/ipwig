package com.azazel.ipwig.uses

import android.content.Context
import com.azazel.ipwig.R
import com.azazel.ipwig.UiConnection
import com.azazel.ipwig.data.ConnectionInfoProvider

/**
 * The uses case of finding the connection.
 */
class FindConnectionInfoUseCase(private val connectionInfoProvider: ConnectionInfoProvider) {

    /**
     * Find the main connection to show in the widget.
     *
     * @param context The calling context.
     * @return The connection to show.
     */
    fun findMainConnectionInfo(context: Context): UiConnection {

        // Get the list of connections.
        val connections = connectionInfoProvider.findConnections(context)
        // Find an ipV4 WIFI type.
        val connection = connections.firstOrNull {
            it.hasWifi && it.ipv4 != null && it.ipv4.isNotEmpty()
        }

        return if (connection != null) {
            // A wifi IPV4 network was found. Note we filtered out empty or nulls.
            UiConnection(connection.ipv4!!)
        } else {
            // If no wifi, find any cellular, otherwise there is no network
            val connectionInfo = connections.firstOrNull { it.hasData }
            if (connectionInfo != null) {
                UiConnection(context.getString(R.string.on_cellular))
            } else {
                UiConnection(context.getString(R.string.no_network))
            }
        }
    }
}
