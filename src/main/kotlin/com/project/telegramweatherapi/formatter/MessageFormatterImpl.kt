package com.project.telegramweatherapi.formatter

import com.project.telegramweatherapi.formatter.icon.Icon
import com.project.telegramweatherapi.userdata.model.UserData
import com.project.telegramweatherapi.weather.model.WeatherInfo
import com.vdurmont.emoji.EmojiParser
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Component
class MessageFormatterImpl : MessageFormatter {

    companion object {
        const val WEATHER_ID_CLOUD: Int = 801
        const val WEATHER_ID_SUNNY: Int = 800
        const val WEATHER_ID_ATMOSPHERE: Int = 700 //mist, smoke, haze etc
        const val WEATHER_ID_SNOW: Int = 600
        const val WEATHER_ID_RAIN: Int = 300
        const val WEATHER_ID_LIGHTNING: Int = 200
    }


    override fun addSmiles(text: String): String {
        return EmojiParser.parseToUnicode(text)
    }

    /**
     * Helps to format JSON response from weather API into pretty-structured message for telegram
     */
    override fun formatWeatherMessage(weatherInfo: WeatherInfo): String {
        val weatherIcon: String = getWeatherIcon(weatherInfo.weather[0].id)
        val temperature: String = if (weatherInfo.main.temp.toInt() > 0) { // add "+" to temp if above zero
            "+" + weatherInfo.main.temp.toInt().toString()
        } else {
            weatherInfo.main.temp.toInt().toString()
        }
        val formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(weatherInfo.dt * 1000 + weatherInfo.timezone * 1000),
            ZoneOffset.UTC
        )
        val dateAsString = date.format(formatter)
        val formattedMessage = "Weather in city of ${weatherInfo.name} \n" +
                "${Icon.DATE.value} Date: $dateAsString \n" +
                "$weatherIcon " + "${weatherInfo.weather[0].description.replaceFirstChar { o -> o.uppercaseChar() }} \n" +
                "${Icon.THERMOMETER.value} Temperature: $temperature \n " +
                "${Icon.THERMOMETER.value} Feels like: ${weatherInfo.main.feels_like.toInt()} \n" +
                "${Icon.DROPLET.value} Humidity: ${weatherInfo.main.humidity}% \n"
        return addSmiles(formattedMessage)
    }

    override fun formatUserData(userData: UserData): String {
        var data = userData.registeredAt.truncatedTo(ChronoUnit.SECONDS).toString()
        data = data.replace("T", " ")
        return "User ID: ${userData.userId} \n" +
                "First name: ${userData.firstName} \n" +
                "Last name: ${userData.lastName} \n" +
                "User name: @${userData.userName} \n" +
                "Date of registering in Weather bot: $data"
    }

    private fun getWeatherIcon(weatherId: Int): String {
        if (weatherId >= WEATHER_ID_CLOUD) {
            return Icon.CLOUD.value
        }
        if (weatherId == WEATHER_ID_SUNNY) {
            return Icon.SUNNY.value
        }
        if (weatherId > WEATHER_ID_ATMOSPHERE) {
            return Icon.CLOUD.value
        }
        if (weatherId >= WEATHER_ID_SNOW) {
            return Icon.SNOW.value
        }
        if (weatherId >= WEATHER_ID_RAIN) {
            return Icon.RAIN.value
        }
        if (weatherId >= WEATHER_ID_LIGHTNING) {
            return Icon.LIGHTNING.value
        }
        return Icon.PARTLY_SUNNY.value //default value
    }
}