package dev.kirakun.khealthcenter.data

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ChangesTokenRequest
import dev.kirakun.khealthcenter.api.RetrofitClient
import dev.kirakun.khealthcenter.utils.Common
import okhttp3.MediaType
import okhttp3.RequestBody

class HealthConnectManager(private val context: Context) {
    private val client by lazy { HealthConnectClient.getOrCreate(context) }
    private val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
    )

    suspend fun checkPermissions(): Boolean {
        return client.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    suspend fun startMonitoring() {
        var changesToken = client.getChangesToken(
            ChangesTokenRequest(recordTypes = setOf(HeartRateRecord::class))
        )

        while (true) {
            changesToken = processChanges(changesToken)
            // Common.retrofitService.getHealth().execute()

            Thread.sleep(1000)
        }
    }

    private suspend fun processChanges(token: String): String {
        var nextChangesToken = token
        do {
            val response = client.getChanges(nextChangesToken)
            response.changes.forEach { change ->
                when (change) {
                    is UpsertionChange -> processUpsertionChange(change)
                    is DeletionChange -> processDeletionChange(change)
                }
            }
            nextChangesToken = response.nextChangesToken
        } while (response.hasMore)

        return nextChangesToken
    }

    private fun processDeletionChange(change: DeletionChange) {
    }

    private fun processUpsertionChange(change: UpsertionChange) {
        val client = RetrofitClient.getClient(context) ?: return
        if (change.record !is HeartRateRecord)
            return;

        val record = (change.record as HeartRateRecord)

        client.postHeartRate(
            RequestBody.create(MediaType.parse("text/plain"), change.record.metadata.id),
            RequestBody.create(MediaType.parse("text/plain"), record.metadata.dataOrigin.packageName),
            record.metadata.recordingMethod,
            Common.toDotNetTicks(record.startTime),
            record.startZoneOffset?.totalSeconds ?: 0,
            Common.toDotNetTicks(record.endTime),
            record.endZoneOffset?.totalSeconds ?: 0,
            RequestBody.create(MediaType.parse("text/plain"), record.samples.joinToString("|") { "${Common.toDotNetTicks(it.time)}:${it.beatsPerMinute}" })
        ).execute()
    }
}