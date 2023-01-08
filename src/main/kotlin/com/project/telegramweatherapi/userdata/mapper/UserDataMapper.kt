package com.project.telegramweatherapi.userdata.mapper

import com.project.telegramweatherapi.userdata.model.UserData
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserDataMapper {

    @Insert("INSERT INTO USERDATA_TABLE " +
            "VALUES (#{userId}, #{firstName}, #{lastName}, #{userName}, #{registeredAt})")
    fun saveUserData(userData: UserData)

    @Select("SELECT * FROM USERDATA_TABLE WHERE USER_ID=#{userId}")
    fun getUserData(userId: Long) : UserData

    @Delete("DELETE FROM USERDATA_TABLE WHERE USER_ID=#{userId}")
    fun deleteUserData(userId: Long)

    @Select("SELECT COUNT(USER_ID) FROM USERDATA_TABLE WHERE USER_ID=#{userId}")
    fun checkUserData(userId: Long): Int
}