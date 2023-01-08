package com.project.telegramweatherapi.weather.model.params

import com.fasterxml.jackson.annotation.JsonProperty

data class Rain(
    @JsonProperty("1h")
    val oneH: Float
)