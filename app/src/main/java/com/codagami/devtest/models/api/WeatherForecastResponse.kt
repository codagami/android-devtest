package com.codagami.devtest.models.api

import com.fasterxml.jackson.annotation.JsonProperty

data class WeatherForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherForecastItem>,
    val message: Int
) {
    data class City(
        val coord: Coord,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val sunrise: Int,
        val sunset: Int,
        val timezone: Int
    ) {
        data class Coord(
            val lat: Double,
            val lon: Double
        )
    }

    data class WeatherForecastItem (
        val clouds: Clouds,
        val dt: Int,
        @JsonProperty("dt_txt")
        val dtTxt: String,
        val main: Main,
        val pop: Double,
        val rain: Rain?,
        val sys: Sys,
        val visibility: Int,
        val weather: List<Weather>,
        val wind: Wind
    ) {
        data class Clouds(
            val all: Int
        )

        data class Main(
            @JsonProperty("feels_like")
            val feelsLike: Double,
            @JsonProperty("grnd_level")
            val grndLevel: Int,
            val humidity: Int,
            val pressure: Int,
            @JsonProperty("sea_level")
            val seaLevel: Int,
            val temp: Double,
            @JsonProperty("temp_kf")
            val tempKf: Double,
            @JsonProperty("temp_max")
            val tempMax: Double,
            @JsonProperty("temp_min")
            val tempMin: Double
        )

        data class Rain(
            @JsonProperty("3h")
            val h: Double
        )

        data class Sys(
            val pod: String
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )

        data class Wind(
            val deg: Int,
            val gust: Double,
            val speed: Double
        )
    }
}