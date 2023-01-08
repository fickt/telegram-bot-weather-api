package com.project.telegramweatherapi


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TelegramWeatherApiApplication

fun main(args: Array<String>) {
	runApplication<TelegramWeatherApiApplication>(*args)
}
