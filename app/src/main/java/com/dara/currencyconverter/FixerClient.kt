package com.dara.currencyconverter

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Creates a Retrofit object
 */

object FixerClient {

    private const val BASE_URL = "http://data.fixer.io/api/"

    /**
     * Creates [HttpLoggingInterceptor] object for logging purposes
     */
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


    /**
     * Creates [OkHttpClient] object for sending and receiving requests
     */
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request()
                val url = request.url().newBuilder()
                    .addQueryParameter("access_key", BuildConfig.FixerApiKey).build()
                it.proceed(request.newBuilder().url(url).build())
            }
            .addInterceptor(getLoggingInterceptor())
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    /**
     * Generates an implementation of the [FixerService] interface
     */
    fun getService(): FixerService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FixerService::class.java)
    }
}