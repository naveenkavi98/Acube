package com.square.acube.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.square.acube.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestUtils {
    companion object {
        @Throws(Exception::class)
        fun getOkkHttpClient(headerModel: HeaderModel?): OkHttpClient {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
                var builder: Request.Builder? = null
                builder = originalRequest.newBuilder()
                if (headerModel?.authToken != null) builder.header(
                    Constants.HEADER_AUTHORISATION,
                    "Bearer " + headerModel.authToken
                )
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }.readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //TODO: before_production
                .addNetworkInterceptor(StethoInterceptor())
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
            return okHttpClient
        }

        @Throws(Exception::class)
        fun makeHttpRequest(okHttpClient: OkHttpClient?): Retrofit? {
            return if (okHttpClient == null) {
                null
            } else {
                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setLenient()
                    .create()
                Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }
        }

        fun getOkkHttpClient(): OkHttpClient {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
                var builder: Request.Builder? = null
                builder = originalRequest.newBuilder()
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }.readTimeout(15, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(StethoInterceptor())
                .writeTimeout(15, TimeUnit.MINUTES)
                .connectTimeout(15, TimeUnit.MINUTES)
                .build()
            return okHttpClient
        }
    }
}