package com.avinash.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.avinash.weather.data.network.WeatherApi
import com.avinash.weather.data.repository.DefaultWeatherRepository
import com.avinash.weather.data.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(weatherApi: WeatherApi): WeatherRepository =
        DefaultWeatherRepository(weatherApi)
}