package dev.kirakun.khealthcenter.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat


class BootReceiver : BroadcastReceiver () {
    override fun onReceive(context: Context, intent: Intent?) {
        val serviceIntent = Intent(context, HealthService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}