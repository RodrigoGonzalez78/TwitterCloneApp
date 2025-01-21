package com.example.twittercloneapp.data.remote

import com.example.twittercloneapp.data.remote.dto.LoginResponse
import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body userIdentifier:UserDto):LoginResponse

    @POST("registration")
    suspend fun register(@Body userIdentifier: UserDto)

    @POST("rectweet")
    suspend fun newTweet(@Header("Authorization") token: String, @Body tweet: TweetDto)


    companion object {
        const val BASE_URL = "http://192.168.91.237:8080"
    }
}