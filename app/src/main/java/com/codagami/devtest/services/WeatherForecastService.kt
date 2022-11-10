package com.codagami.devtest.services

import com.codagami.devtest.models.Constants
import com.codagami.devtest.models.api.WeatherForecastResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastService {
    @GET("data/2.5/forecast")
    fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("units") units: String = "imperial", @Query("appid") appId : String = Constants.OpenWeatherAPIKey): Call<WeatherForecastResponse>
}

/**
 * Service that enables the ability to fetch forecasts using the OpenWeather API
 */
class WeatherForecastRequestService {

    /**
     * Interface that wraps a call when weather forecast data has been received
     */
    interface WeatherForecastListener {

        /**
         * Triggered when weather forecast data has been received from the API
         */
        fun onWeatherForecastReceived(forecast: WeatherForecastResponse.WeatherForecastItem)
    }

    companion object {

        /**
         * Provides the 5 day / 3 hour forecast of a given lat/long
         */
        fun getForecast(service: WeatherForecastService, latitude: Double, longitude: Double, listener: WeatherForecastListener? = null) {
            val forecastCall = service.getForecast(latitude, longitude)

            forecastCall.enqueue(object: Callback<WeatherForecastResponse> {
                override fun onResponse( call: Call<WeatherForecastResponse>, response: Response<WeatherForecastResponse>) {
                    if(response.isSuccessful) {
                        val forecastList = response.body()!!

                        forecastList.list.forEach { forecast -> listener?.onWeatherForecastReceived(forecast) }
                    }
                }

                override fun onFailure(call: Call<WeatherForecastResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}