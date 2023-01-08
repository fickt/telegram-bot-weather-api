package com.project.telegramweatherapi.userdata.service

import com.project.telegramweatherapi.userdata.model.UserData
import org.telegram.telegrambots.meta.api.objects.Message

interface UserDataService {
    fun saveUserData(message: Message)

    fun deleteUserData(message: Message)

    fun getUserData(message: Message): UserData
}