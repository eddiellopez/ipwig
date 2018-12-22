package com.azazel.ipwig

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.RemoteViews
import java.net.Inet4Address

private const val TAG = "IpWidgetProvider"

/**
 * Implementation of App Widget functionality.
 */
class IpWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        updateView(context, appWidgetIds, appWidgetManager)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        /**
         * Updates the views.
         * @param context The calling context.
         * @param appWidgetIds The widget IDs.
         * @param appWidgetManager The Widget Manager.
         */
        internal fun updateView(
            context: Context,
            appWidgetIds: IntArray,
            appWidgetManager: AppWidgetManager
        ) {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            // Get the list of Ip Addresses
            val ips: List<String> = findIpAddresses(cm)

            val ipString = if (ips.isEmpty()) {
                context.getString(R.string.no_network)
            } else {
                // Use the first one for now
                ips[0]
            }

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, ipString)
            }
        }

        /**
         * Finds a list of IPv4 addresses that are connected, not broadcast and not loopback.
         *
         * @param connectivityManager The connectivity manager
         * @return The list of IP addresses, if any
         */
        private fun findIpAddresses(connectivityManager: ConnectivityManager): List<String> {
            // TODO: Try `activeNetwork`
            val networkList = connectivityManager.allNetworks.asList()
            val ips: MutableList<String> = mutableListOf()

            networkList.forEach {
                val linkProperties = connectivityManager.getLinkProperties(it)
                val networkInfo = connectivityManager.getNetworkInfo(it)

                Log.i(TAG, "Is connected: " + networkInfo.isConnected)
                Log.i(TAG, "Interface name: " + linkProperties.interfaceName)
                Log.i(TAG, "Link Address: " + linkProperties.linkAddresses.toString())

                if (networkInfo.isConnected) {
                    // There may be multiple widgets active, so update all of them
                    linkProperties.linkAddresses.filter { la ->
                        (la.address is Inet4Address
                                && !la.address.isLoopbackAddress
                                && !la.address.isMulticastAddress)
                    }.forEach { e -> ips.add(e.address.hostAddress) }
                }
            }
            return ips
        }

        private fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int, text: String
        ) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.ip_widget_provider)
            views.setTextViewText(R.id.appwidget_text, text)

            // Launch MainActivity when the widget body is clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

            // Refresh the IP text when the refresh button is clicked
            val refreshIntent = Intent().also {
                it.action = "com.azazel.ipwig.UPDATE_IP"
                it.setClass(context, UpdateIpReceiver::class.java)
            }
            val pendingIntentRefresh = PendingIntent.getBroadcast(
                context, PendingIntent.FLAG_UPDATE_CURRENT,
                refreshIntent, 0
            )
            views.setOnClickPendingIntent(R.id.refresh_button, pendingIntentRefresh)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

