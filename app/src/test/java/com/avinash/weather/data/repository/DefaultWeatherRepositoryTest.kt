package com.avinash.weather.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import com.avinash.weather.data.model.toWeather
import com.avinash.weather.data.network.WeatherApi
import com.avinash.weather.data.sampleForecastResponse
import com.avinash.weather.model.Weather
import com.avinash.weather.utils.Result
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DefaultWeatherRepositoryTest {
    private lateinit var repository: DefaultWeatherRepository
    private val weatherApi = mockk<WeatherApi>()

    @Before
    fun setup() {
        repository = DefaultWeatherRepository(weatherApi)
    }

    @Test
    fun `when getWeatherForecast is called, it should emit loading state and then success state`() =
        runTest {
            coEvery {
                weatherApi.getWeatherForecast(
                    any(),
                    any(),
                    any()
                )
            } returns sampleForecastResponse

            val results = mutableListOf<Result<Weather>>()
            repository.getWeatherForecast("Munich").collect { result ->
                results.add(result)
            }
            assertEquals(Result.Loading, results[0])

            assertEquals(Result.Success(sampleForecastResponse.toWeather()), results[1])
        }
}