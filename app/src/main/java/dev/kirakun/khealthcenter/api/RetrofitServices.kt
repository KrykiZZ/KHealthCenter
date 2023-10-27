package dev.kirakun.khealthcenter.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @Multipart
    @POST("me")
    fun postMe(
        @Part("token") token: RequestBody
    ): Call<String>

    @Multipart
    @POST("heartrate")
    fun postHeartRate(
        @Part("health_connect_id") healthConnectId: RequestBody,
        @Part("data_origin") dataOrigin: RequestBody,
        @Part("recording_method") recordingMethod: Int,
        @Part("start_time") startTime: Long,
        @Part("start_zone_offset") startZoneOffset: Int,
        @Part("end_time") endTime: Long,
        @Part("end_zone_offset") endZoneOffset: Int,
        @Part("samples") samples: RequestBody
    ): Call<Void>

    @Multipart
    @POST("health")
    fun postHealth(
        @Part("sdk") sdk: Int,
        @Part("model") model: RequestBody,

        @Part("manufacturer") manufacturer: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("device") device: RequestBody,

        @Part("board") board: RequestBody,

        @Part("display") display: RequestBody,
        @Part("fingerprint") fingerprint: RequestBody,

        @Part("product") product: RequestBody,
        @Part("socManufacturer") socManufacturer: RequestBody,
        @Part("socModel") socModel: RequestBody,
    ): Call<Void>
}