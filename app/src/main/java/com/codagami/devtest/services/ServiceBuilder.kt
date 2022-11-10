package com.codagami.devtest.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory that provides RetroFit2 services
 */
object ServiceBuilder {
    private const val URL = "api.openweathermap.org"
    private val httpClient = OkHttpClient.Builder()

    /**
     * Constructs a RetroFit2 service based on its type
     */
    fun <T> buildService(serviceType: Class<T>) : T {
        val builder = Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        val retroFit = builder.build()

        return retroFit.create(serviceType)
    }
}