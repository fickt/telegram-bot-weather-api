package com.project.telegramweatherapi.config.bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class BotConfig {
    @Value("\${bot.name}")
    val botName: String = "defaultBotName"

    @Value("\${bot.token}")
    val botToken: String = "defaultBotToken"
}