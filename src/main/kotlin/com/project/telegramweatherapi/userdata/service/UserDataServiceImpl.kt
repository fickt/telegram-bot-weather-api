package com.project.telegramweatherapi.userdata.service

import com.project.telegramweatherapi.userdata.model.UserData
import com.project.telegramweatherapi.userdata.mapper.UserDataMapper
import com.project.telegramweatherapi.userdata.exception.UserDataNotFoundException
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.ZoneId
import java.time.ZonedDateTime


@Service
class UserDataServiceImpl(val userDataMapper: UserDataMapper) : UserDataService {
    companion object {
        const val ZONE_ID: String = "Europe/Moscow"
    }

    override fun saveUserData(message: Message) {
        val zone = ZoneId.of(ZONE_ID)
        val userData = UserData(
            message.chatId,
            message.from.firstName,
            message.from.lastName,
            message.from.userName,
            ZonedDateTime.now(zone).toLocalDateTime()
        )
        userDataMapper.saveUserData(userData)
    }

    override fun getUserData(message: Message): UserData {
        if (checkUserData(message)) {
            return userDataMapper.getUserData(message.chatId)
        }
        throw UserDataNotFoundException()
    }

    override fun deleteUserData(message: Message) {
        val userId = message.chatId
        userDataMapper.deleteUserData(userId)
    }

    private fun checkUserData(message: Message): Boolean {
        if (userDataMapper.checkUserData(message.chatId) > 0) {
            return true
        }
        return false
    }
}