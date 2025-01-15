package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName


data class LoginResponse (
    @SerializedName("user_id") val userID: String,
    @SerializedName("token") val token: String
)