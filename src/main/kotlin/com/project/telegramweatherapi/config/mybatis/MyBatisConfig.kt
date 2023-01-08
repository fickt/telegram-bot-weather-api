package com.project.telegramweatherapi.config.mybatis

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration


@Configuration
@MapperScan("com.project.telegramweatherapi.userdata.mapper")
class MyBatisConfig
