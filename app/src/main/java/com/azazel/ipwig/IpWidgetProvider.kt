package com.azazel.ipwig

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.RemoteViews

private const val TAG = "IpWidgetProvider"

/**
 * Implementation of App Widget functionality.
 */
class IpWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.i(TAG, "onUpdate()")
        updateView(context)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        /**
         * Updates the views of all the widgets.
         *
         * @param context The calling context.
         */
        internal fun updateView(
            context: Context
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, IpWidgetProvider::class.java))
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val connections = ConnectionUtils.findConnections(cm)

            // Initialize ip string as empty
            var ipString = ""

            // Find an ipV4 WIFI type, useful for adb connect
            val connection = connections.firstOrNull { it.hasWifi && it.ipv4.isNotEmpty() }
            if (connection != null) {
                ipString = connection.ipv4
            }

            if (ipString.isEmpty()) {
                // No wifi, find any cellular, otherwise there is no network
                val orNull = connections.firstOrNull { it.hasData }
                ipString = if (orNull != null) {
                    context.getString(R.string.on_cellular)
                } else {
                    context.getString(R.string.no_network)
                }
            }

            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, ipString)
            }
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
            val pendingIntentRefresh = UpdateIpReceiver.getPendingIntent(context)
            views.setOnClickPendingIntent(R.id.refresh_button, pendingIntentRefresh)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

