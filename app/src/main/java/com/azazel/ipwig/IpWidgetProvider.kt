package com.azazel.ipwig

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.azazel.ipwig.data.ConnectionInfoProvider
import com.azazel.ipwig.uses.FindConnectionInfoUseCase

private const val TAG = "IpWidgetProvider"

/**
 * Implementation of App Widget functionality.
 */
class IpWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
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
            val uiConnection = FindConnectionInfoUseCase(ConnectionInfoProvider())
                .findMainConnectionInfo(context)

            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    IpWidgetProvider::class.java
                )
            ).forEach {
                updateAppWidget(context, appWidgetManager, it, uiConnection.text)
            }
        }

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            ipAddress: String
        ) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.ip_widget_provider)
            views.setTextViewText(R.id.appwidget_text, ipAddress)

            // Launch MainActivity when the widget body is clicked.
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

            // Refresh the IP text when the refresh button is clicked.
            val pendingIntentRefresh = UpdateIpReceiver.getPendingIntent(context)
            views.setOnClickPendingIntent(R.id.refresh_button, pendingIntentRefresh)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

