package dev.kirakun.khealthcenter

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import dev.kirakun.khealthcenter.api.RetrofitClient
import dev.kirakun.khealthcenter.background.HealthService
import dev.kirakun.khealthcenter.utils.Notifications
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
//    private lateinit var hcManager: HealthConnectManager
//
//    private val permissions = setOf(
//        HealthPermission.getReadPermission(HeartRateRecord::class),
//        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
//    )

    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settings = applicationContext.getSharedPreferences("KHC_GENERAL", 0)

//        val tokenInput = findViewById<EditText>(R.id.inputToken)
//        tokenInput.setText(settings.getString("KHC_TOKEN", null))
//
//        val buttonSave = findViewById<Button>(R.id.buttonSave)
//        buttonSave.setOnClickListener {
//            val editor = settings.edit()
//
//            editor.putString("KHC_TOKEN", tokenInput.text.toString())
//            editor.apply()
//        }

        val serviceIntent = Intent(this, HealthService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)

        // hcManager = HealthConnectManager(this)
        // GlobalScope.launch { checkPermissionsAndRun() }
    }

//    private fun createChannel() {
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val channel = NotificationChannel("CHANNEL_ID", "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH)
//        channel.description = "CHANNEL_DESC"
//        manager.createNotificationChannel(channel)
//    }
//
//    private fun showNotification() {
//        val n = NotificationCompat.Builder(this, "CHANNEL_ID")
//            .setContentTitle("TITLE")
//            .setContentText("TEXT")
//            .setSmallIcon(R.drawable.ic_notification)
//            .setOngoing(true)
//            .build()
//
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        manager.notify(0, n)
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    private suspend fun onAllPermissionsGiven() {
//        Log.d("SOMETHING", "WOW IM ON TV!")
//
//        val serviceIntent = Intent(this, MyService::class.java)
//        ContextCompat.startForegroundService(this, serviceIntent)
//    }

//    private suspend fun readAll() {
//        val response =
//            healthConnectManager.client.readRecords(
//                ReadRecordsRequest(
//                    HeartRateRecord::class,
//                    timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(5*60*60*24), Instant.now().plusSeconds(5*60*60*24))
//                )
//            )
//        for (hrRecord in response.records) {
//            for (sample in hrRecord.samples) {
//                Log.d("SOMETHING", "%s BPM".format(sample.beatsPerMinute))
//            }
//        }
//    }

//    private suspend fun checkPermissionsAndRun() {
//        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
//        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
//            if (!granted.containsAll(permissions)) {
//                Toast.makeText(this, "Not all required permissions granted.", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//            else { lifecycleScope.launch { onAllPermissionsGiven() } }
//        }
//
//        val granted = hcClient.permissionController.getGrantedPermissions()
//        if (!granted.containsAll(permissions)) {
//            requestPermissions.launch(permissions)
//        } else { lifecycleScope.launch { onAllPermissionsGiven() } }
//    }
}