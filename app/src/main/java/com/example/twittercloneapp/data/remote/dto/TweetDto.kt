package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TweetDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("userId") val userID: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("date") val date: String? = null
)
