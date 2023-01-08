package com.project.telegramweatherapi.weather.model.params

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)