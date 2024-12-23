package com.avinash.weather.data.repository

import com.avinash.weather.model.Weather
import com.avinash.weather.utils.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecast(city: String): Flow<Result<Weather>>
}