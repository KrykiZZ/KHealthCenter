package dev.kirakun.khealthcenter.api

import android.content.Context
import android.os.Build
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://khc.kirakun.dev/"
    private lateinit var token: String
    private var retrofit: Retrofit? = null

    fun getClient(context: Context): RetrofitServices? {
        var client: RetrofitServices? = null

        if (retrofit == null) {
            val settings = context.getSharedPreferences("KHC_GENERAL", 0)
            token = settings.getString("KHC_TOKEN", null) ?: return null

            val okClient = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "KHC $token")
                    .build()
                chain.proceed(newRequest)
            }.build()

            retrofit = Retrofit.Builder()
                .client(okClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            client = retrofit!!.create(RetrofitServices::class.java)
            val result = client.postMe(RequestBody.create(MediaType.parse("text/plain"), token)).execute()
            if (result.body() == "NOPE")
                return null
        }

        return client ?: retrofit!!.create(RetrofitServices::class.java)
    }
}