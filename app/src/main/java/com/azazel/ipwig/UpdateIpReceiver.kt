package com.azazel.ipwig

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "UpdateIpReceiver"
private const val ACTION_UPDATE = "com.azazel.ipwig.UPDATE_IP"

/**
 * A broadcast receiver to handle requests to refresh the Widget UI.
 */
class UpdateIpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the Refresh button is clicked on a widget
        Log.i(TAG, "Refreshing IP...")

        IpWidgetProvider.updateView(context)
    }

    companion object {
        /**
         * Provides a pending intent to start this Broadcast Receiver.
         *
         * @param context The calling context.
         * @return The pending intent.
         */
        fun getPendingIntent(context: Context): PendingIntent {
            val refreshIntent = Intent().also {
                it.action = ACTION_UPDATE
                it.setClass(context, UpdateIpReceiver::class.java)
            }
            return PendingIntent.getBroadcast(
                context, PendingIntent.FLAG_UPDATE_CURRENT,
                refreshIntent, 0
            )
        }
    }
}
