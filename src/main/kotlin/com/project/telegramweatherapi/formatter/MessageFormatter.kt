package com.project.telegramweatherapi.formatter


import com.project.telegramweatherapi.userdata.model.UserData
import com.project.telegramweatherapi.weather.model.WeatherInfo


interface MessageFormatter {
    fun addSmiles(text: String): String
    fun formatWeatherMessage(weatherInfo: WeatherInfo): String
    fun formatUserData(userData: UserData): String
}