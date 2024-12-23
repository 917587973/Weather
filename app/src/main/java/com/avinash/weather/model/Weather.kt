package com.avinash.weather.model

import com.avinash.weather.data.model.ForecastResponse.Current.Condition

data class Weather(
    val temperature: Int,
    val date: String,
    val wind: Int,
    val humidity: Int,
    val feelsLike: Int,
    val condition: Condition,
    val uv: Int,
    var name: String,
    val forecasts: List<Forecast>
)
