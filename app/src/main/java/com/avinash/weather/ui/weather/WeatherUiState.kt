package com.avinash.weather.ui.weather

import com.avinash.weather.model.Weather

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)
