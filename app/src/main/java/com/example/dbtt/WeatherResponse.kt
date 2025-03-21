package com.example.dbtt

data class WeatherResponse(
    val coord: Coord,
    val weather: List<WeatherCondition>,
    val main: MainWeather,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainWeather(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
