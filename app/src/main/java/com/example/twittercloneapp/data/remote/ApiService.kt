package com.example.twittercloneapp.data.remote

import com.example.twittercloneapp.data.remote.dto.LoginResponse
import com.example.twittercloneapp.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST
    suspend fun login(@Body userIdentifier:UserDto):LoginResponse

    @POST
    suspend fun register(@Body userIdentifier: UserDto)



    companion object {
        const val BASE_URL = "http://192.168.91.237:8080"
    }
}