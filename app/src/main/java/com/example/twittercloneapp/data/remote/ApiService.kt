package com.example.twittercloneapp.data.remote

import com.example.twittercloneapp.data.remote.dto.LoginResponse
import com.example.twittercloneapp.data.remote.dto.RelationStatusResponse
import com.example.twittercloneapp.data.remote.dto.ReturnTweetsFollowers
import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.data.remote.dto.UserDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body userIdentifier: UserDto): LoginResponse

    @POST("registration")
    suspend fun register(@Body userIdentifier: UserDto)

    @POST("tweet")
    suspend fun newTweet(@Header("Authorization") token: String, @Body tweet: TweetDto)

    @GET("/viewprofile")
    suspend fun viewProfile(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): UserDto

    @PUT("/modifyProfile")
    suspend fun modifyProfile(@Header("Authorization") token: String, @Body userDto: UserDto)

    @GET("readtweets")
    suspend fun readTweets(
        @Query("id") id: String,
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): List<TweetDto>

    @DELETE("/deleteTweet")
    suspend fun deleteTweet(
        @Query("id") id: String,
        @Query("userId") userId: String
    )

    @Multipart
    @POST("/uploadAvatar")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("/uploadBanner")
    suspend fun uploadBanner(
        @Part banner: MultipartBody.Part
    )

    @GET("/getAvatar")
    suspend fun getAvatar(
        @Query("id") id: String
    )

    @GET("/getBanner")
    suspend fun getBanner(
        @Query("id") id: String
    )

    @POST("/highRelation")
    suspend fun createRelation(
        @Query("id") id: String,
        @Header("Authorization") token: String
    )

    @DELETE("/downRelation")
    suspend fun deleteRelation(
        @Query("id") id: String,
        @Header("Authorization") token: String
    )

    @GET("/consultRelation")
    suspend fun consultRelation(
        @Query("id") id: String,
        @Header("Authorization") token: String
    ): RelationStatusResponse

    @GET("/listUsers")
    suspend fun listUsers(
        @Query("page") page: Int,
        @Query("type") type: String?,
        @Query("search") search: String?,
        @Header("Authorization") token: String
    ): List<UserDto>?

    @GET("/readTweetsFollowers")
    suspend fun readTweetsFollowers(
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): List<ReturnTweetsFollowers>

    companion object {
        const val BASE_URL = "http://192.168.100.7:8080"
    }
}