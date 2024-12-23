package com.avinash.weather.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("weather_pref_main_card", Context.MODE_PRIVATE)

    fun saveCity(city: String) {
        sharedPreferences.edit().putString("city", city).apply()
    }

    fun getCity(): String? {
        return sharedPreferences.getString("city", null)
    }
}
