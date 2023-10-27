package dev.kirakun.khealthcenter.background

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import dev.kirakun.khealthcenter.api.RetrofitClient
import dev.kirakun.khealthcenter.data.HealthConnectManager
import dev.kirakun.khealthcenter.utils.Notifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.time.Instant


class HealthService : Service() {
    private lateinit var hcManager: HealthConnectManager
    private val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
    )

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("KHC", "Service started.")
        hcManager = HealthConnectManager(this)

        Notifications.createChannel(this)
        GlobalScope.launch {
            Notifications.showPermissionsNotification(applicationContext)
        }
//        hcManager.checkPermissions(permissions) {
//
//        }
//        Thread {
//            if (!) {
//                Notifications.showPermissionsNotification(this)
//            }
//        }



        startForeground(Notifications.INFO_NOTIFICATION_ID, Notifications.createPermanentNotification(this, 86, 98, 1200, 6000))

        Thread { deviceHealthThread() }.start()
        // Thread { deviceHealthThread() }.start()

        return START_STICKY
    }

    private fun deviceHealthThread() {
        while (true) {
            try {
                val client = RetrofitClient.getClient(this) ?: continue
                client.postHealth(
                    Build.VERSION.SDK_INT,
                    RequestBody.create(MediaType.parse("text/plain"), Build.MODEL),

                    RequestBody.create(MediaType.parse("text/plain"), Build.MANUFACTURER),
                    RequestBody.create(MediaType.parse("text/plain"), Build.BRAND),
                    RequestBody.create(MediaType.parse("text/plain"), Build.DEVICE),

                    RequestBody.create(MediaType.parse("text/plain"), Build.BOARD),

                    RequestBody.create(MediaType.parse("text/plain"), Build.DISPLAY),
                    RequestBody.create(MediaType.parse("text/plain"), Build.FINGERPRINT),

                    RequestBody.create(MediaType.parse("text/plain"), Build.PRODUCT),
                    RequestBody.create(MediaType.parse("text/plain"), Build.SOC_MANUFACTURER),
                    RequestBody.create(MediaType.parse("text/plain"), Build.SOC_MODEL)
                ).execute()
            }
            catch (e: Exception) { Log.d("KHC", e.toString()) }

            Notifications.showPermanentNotification(this, (0..100).random(), (0..100).random(), (0..100).random(), (0..100).random())
            Thread.sleep(5*60*1000)
        }
    }
}