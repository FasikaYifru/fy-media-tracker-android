package edu.metrostate.ics342.mediatracker.data.network

import edu.metrostate.ics342.mediatracker.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults    = true
    }

    private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val client by lazy {
        OkHttpClient.Builder().addInterceptor(loggingInterceptor()).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val userApiService: UserApiService by lazy { retrofit.create(UserApiService::class.java) }

    fun mediaApiService(sessionRepository: SessionRepository): MediaApiService =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(sessionRepository))
                    .addInterceptor(loggingInterceptor())
                    .build()
            )
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MediaApiService::class.java)

    fun reviewApiService(sessionRepository: SessionRepository): ReviewApiService =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(sessionRepository))
                    .addInterceptor(loggingInterceptor())
                    .build()
            )
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ReviewApiService::class.java)
}