package com.project.telegramweatherapi.userdata.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class UserData(
    var userId: Long,
    var firstName: String,
    var lastName: String,
    var userName: String,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var registeredAt: LocalDateTime
)