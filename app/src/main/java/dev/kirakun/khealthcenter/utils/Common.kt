package dev.kirakun.khealthcenter.utils

import dev.kirakun.khealthcenter.api.RetrofitClient
import dev.kirakun.khealthcenter.api.RetrofitServices
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime


object Common {
    private val dotNetEpoch = ZonedDateTime.of(
        1, 1, 1, 0, 0, 0, 0,
        ZoneOffset.UTC
    ).toInstant()

    fun toDotNetTicks(instant: Instant): Long {
        val secsDiff = Math.subtractExact(instant.epochSecond, dotNetEpoch.epochSecond)
        val totalHundredNanos = secsDiff * 10000000
        return Math.addExact(totalHundredNanos, ((instant.nano - dotNetEpoch.nano) / 100).toLong())
    }
}