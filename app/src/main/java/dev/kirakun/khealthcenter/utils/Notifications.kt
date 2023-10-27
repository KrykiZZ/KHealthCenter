package dev.kirakun.khealthcenter.utils

import android.R.id.message
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import dev.kirakun.khealthcenter.MainActivity
import dev.kirakun.khealthcenter.R
import kotlinx.coroutines.CoroutineScope


object Notifications {
    private const val KHC_CHANNEL_ID: String = "KHC_NOTIFICATIONS"
    const val PERMANENT_NOTIFICATION_ID: Int = 1
    const val INFO_NOTIFICATION_ID: Int = 1

    fun createChannel(context: ContextWrapper) {
        val manager = context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(KHC_CHANNEL_ID, "KHC", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Kira's Health Center"
        manager.createNotificationChannel(channel)
    }

    fun showPermissionsNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, KHC_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("KHC")
            .setContentText("App doesn't have required permissions to work. Click on notification to grant.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(INFO_NOTIFICATION_ID, builder.build())
    }

    fun createPermanentNotification(context: Context, hr: Int, spo2: Int, steps: Int, distance: Int): Notification {
        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_small)
        notificationLayout.setTextViewText(R.id.notification_hr, "HR\n${hr}")
        notificationLayout.setTextViewText(R.id.notification_spo2, "SpO2\n${spo2}")
        notificationLayout.setTextViewText(R.id.notification_steps, "Steps\n${steps}")
        notificationLayout.setTextViewText(R.id.notification_distance, "Distance\n${distance}")

        val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.notification_large)
        notificationLayoutExpanded.setTextViewText(R.id.notification_hr, "HR\n${hr}")
        notificationLayoutExpanded.setTextViewText(R.id.notification_spo2, "SpO2\n${spo2}")
        notificationLayoutExpanded.setTextViewText(R.id.notification_steps, "Steps\n${steps}")
        notificationLayoutExpanded.setTextViewText(R.id.notification_distance, "Distance\n${distance}")

        return NotificationCompat.Builder(context, KHC_CHANNEL_ID)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setSmallIcon(R.drawable.ic_notification)
            .setSilent(true)
            .setOngoing(true)
            .build()
    }

    fun showPermanentNotification(context: ContextWrapper, hr: Int, spo2: Int, steps: Int, distance: Int) {
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(PERMANENT_NOTIFICATION_ID, createPermanentNotification(context, hr, spo2, steps, distance))
    }
}