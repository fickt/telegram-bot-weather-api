package com.project.telegramweatherapi.config.weather

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class WeatherConfig {
    @Value("\${weather.api.key}")
    val weatherApiKey: String = "weatherApiKeyDefaultValue"
    @Value("\${weather.api.uri}")
    val weatherApiUri: String = "weatherApiUriDefaultValue"
}