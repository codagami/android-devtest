package com.codagami.devtest.services

import com.codagami.devtest.models.Constants
import com.codagami.devtest.models.api.CityLookupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CityLookupService {
    @GET("geo/1.0/direct")
    fun getCityLocation(@Query("q") cityStateCountry: String, @Query("limit") limit: Int = 5, @Query("appid") appId : String = Constants.OpenWeatherAPIKey): Call<CityLookupResponse>
}

/**
 * Service that provides a lat/long for a given city, state, country
 * using the OpenWeather API
 */
class CityLookupResponseService {

    /**
     * Interface that wraps the call when a city has been found by its name
     */
    interface CityLookupListener {

        /**
         * Triggered when a city has been identified from the API
         */
        fun onCityLookup(city: CityLookupResponse.CityLookupResponseItem)
    }

    companion object {
        fun getCityLocation(service: CityLookupService, city: String, state: String, country: String, listener: CityLookupListener?) {
            val cityLookupCall = service.getCityLocation("${city},${state},${country}")

            cityLookupCall.enqueue(object: Callback<CityLookupResponse> {
              override fun onResponse(call: Call<CityLookupResponse>, response: Response<CityLookupResponse>) {
                  if(response.isSuccessful) {
                      val cityList = response.body()!!
                      cityList.forEach { city -> listener?.onCityLookup(city) }
                  }
              }

                override fun onFailure(call: Call<CityLookupResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}