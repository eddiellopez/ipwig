package com.azazel.ipwig

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "UpdateIpReceiver"

class UpdateIpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the Refresh button is clicked on a widget
        Log.i(TAG, "Refreshing IP...")

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, IpWidgetProvider::class.java))
        IpWidgetProvider.updateView(context, appWidgetIds, appWidgetManager)
    }
}
