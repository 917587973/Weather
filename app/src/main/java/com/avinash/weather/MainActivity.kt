package com.avinash.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.avinash.weather.ui.theme.WeatherTheme
import com.avinash.weather.ui.weather.WeatherScreen
import com.avinash.weather.utils.DEFAULT_WEATHER_DESTINATION
import com.avinash.weather.utils.PreferencesHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var  preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      preferencesHelper = PreferencesHelper(applicationContext)
        var city = preferencesHelper.getCity()
        if (!city.isNullOrEmpty()){
            DEFAULT_WEATHER_DESTINATION = city
        }
        setContent {
            WeatherTheme {
                WeatherScreen()
            }
        }
    }
}
