package com.project.telegramweatherapi.service

import com.project.telegramweatherapi.config.bot.BotConfig
import com.project.telegramweatherapi.formatter.MessageFormatter
import com.project.telegramweatherapi.userdata.service.UserDataService
import com.project.telegramweatherapi.userdata.exception.UserDataNotFoundException
import com.project.telegramweatherapi.weather.client.WeatherClient
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault

@Service
class TelegramBot(
    private val botConfig: BotConfig,
    private val weatherClient: WeatherClient,
    private val userDataService: UserDataService,
    private val messageFormatter: MessageFormatter
) : TelegramLongPollingBot() {

    init { // creates menu
        val listOfCommands = listOf(
            BotCommand("/start", "get bot started"),
            BotCommand("/help", "a brief instruction how to use bot"),
            BotCommand("/weather", "get weather condition in chosen region"),
            BotCommand("/mydata", "show your personal data"),
            BotCommand("/deletedata", "delete your personal data")
        )
        execute(SetMyCommands(listOfCommands, BotCommandScopeDefault(), null))
    }

    companion object {
        const val START: String = "/start"
        const val START_MESSAGE: String = "Hi, %s! Nice to see you! :blush:"
        const val HELP: String = "/help"
        const val HELP_INSTRUCTION: String = "Weather bot is used for " +
                "getting actual weather forecast in chosen region," +
                " for example you can type  \"/weather Moscow\" and get actual weather conditions in this region, " +
                "come on, give it a try :)"
        const val WEATHER: String = "/weather"
        const val DATA: String = "/mydata"
        const val DELETE_DATA: String = "/deletedata"
        const val UNSUPPORTED_COMMAND: String =
            "Unknown command: %s, you can get list of valid commands by typing /help"
        const val CITY_NOT_FOUND: String = "City %s has not been found!"
        const val NO_COMMAND_GIVEN: String = ":warning: Please mind writing type of operation explicitly, e.g /weather, /mydata etc"
        const val DATA_DELETED_SUCCESSFULLY: String = "Data of user with ID: %s has been deleted :white_check_mark:"
        const val USER_DATA_NOT_FOUND: String = "Data of user with ID: %s probably has already been deleted!"
    }

    override fun getBotToken(): String {
        return botConfig.botToken
    }

    override fun getBotUsername(): String {
        return botConfig.botName
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val message = update.message

            if (!message.hasEntities()) {
                sendErrorNoCommandGiven(message)
                return
            }
            val command: String = message.entities[0].text
            when (command) {
                START -> sayHello(message)
                HELP -> sendInstruction(message)
                WEATHER -> getWeather(message)
                DATA -> getUserData(message)
                DELETE_DATA -> deleteUserData(message)
                else -> sendUnsupportedCommandWarning(message)
            }
        }
    }

    private fun getWeather(message: Message) {
        val cityName: String = message.text.split(" ")[1]
        val responseMessage = SendMessage()
        try {
            val responseText = messageFormatter.formatWeatherMessage(weatherClient.getWeather(cityName))
            responseMessage.text = responseText
            responseMessage.chatId = message.chatId.toString()
            sendResponse(responseMessage)
        } catch (e: Exception) { // if city has not been found
            responseMessage.text = String.format(CITY_NOT_FOUND, cityName)
            responseMessage.chatId = message.chatId.toString()
            sendResponse(responseMessage)
        }
    }

    private fun sendResponse(responseMessage: SendMessage) {
        execute(responseMessage)
    }

    private fun sayHello(message: Message) {
        saveUserData(message)
        val responseMessage = SendMessage()
        val responseText = messageFormatter.addSmiles(START_MESSAGE)
        responseMessage.text = String.format(responseText, message.chat.firstName)
        responseMessage.chatId = message.chatId.toString()
        sendResponse(responseMessage)
    }

    private fun sendInstruction(message: Message) {
        val responseMessage = SendMessage()
        responseMessage.chatId = message.chatId.toString()
        responseMessage.text = HELP_INSTRUCTION
        sendResponse(responseMessage)
    }

    private fun getUserData(message: Message) {
        try {
            val userData = userDataService.getUserData(message)
            val responseMessage = SendMessage()
            responseMessage.chatId = message.chatId.toString()
            responseMessage.text = messageFormatter.formatUserData(userData)
            sendResponse(responseMessage)
        } catch (e: UserDataNotFoundException) {
            val responseMessage = SendMessage()
            responseMessage.chatId = message.chatId.toString()
            responseMessage.text = String.format(USER_DATA_NOT_FOUND, message.chatId)
            sendResponse(responseMessage)
        }
    }

    private fun sendUnsupportedCommandWarning(message: Message) {
        val responseMessage = SendMessage()
        responseMessage.chatId = message.chatId.toString()
        responseMessage.text = String.format(UNSUPPORTED_COMMAND, message.text)

        sendResponse(responseMessage)
    }

    private fun sendErrorNoCommandGiven(message: Message) {
        val responseMessage = SendMessage()
        responseMessage.chatId = message.chatId.toString()
        responseMessage.text = messageFormatter.addSmiles(NO_COMMAND_GIVEN)
        sendResponse(responseMessage)
    }


    private fun saveUserData(message: Message) {
        userDataService.saveUserData(message)
    }

    private fun deleteUserData(message: Message) {
        userDataService.deleteUserData(message)
        val responseMessage = SendMessage()
        responseMessage.chatId = message.chatId.toString()
        responseMessage.text = messageFormatter.addSmiles(String.format(DATA_DELETED_SUCCESSFULLY, message.chatId))
        sendResponse(responseMessage)
    }
}