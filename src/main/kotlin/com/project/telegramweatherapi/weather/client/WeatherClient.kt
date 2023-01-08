package com.project.telegramweatherapi.weather.client

import com.project.telegramweatherapi.config.weather.WeatherConfig
import com.project.telegramweatherapi.weather.model.WeatherInfo
import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI

@Component
@EnableCaching
class WeatherClient(
    private val restTemplate: RestTemplate,
    private val weatherConfig: WeatherConfig
) {

    private val logger = KotlinLogging.logger {}

    @Cacheable("weatherData")
    fun getWeather(cityName: String): WeatherInfo {
        logger.info { "getting weather data not from cache" }
            return restTemplate.getForObject(URI(getURI(cityName)), WeatherInfo::class.java)!!
    }

    private fun getURI(cityName: String): String {
        return String.format(getWeatherApiUri(), cityName, getWeatherApiKey())
    }

    private fun getWeatherApiKey(): String = weatherConfig.weatherApiKey
    private fun getWeatherApiUri(): String = weatherConfig.weatherApiUri

    /**
     * Cleans cache every time fixed in fixedRateString param
     */
    @CacheEvict(value = ["weatherData"], allEntries = true)
    @Scheduled(fixedRateString = "\${caching.spring.weather.data.TTL}")
    protected fun emptyWeatherDataCache(){}
}