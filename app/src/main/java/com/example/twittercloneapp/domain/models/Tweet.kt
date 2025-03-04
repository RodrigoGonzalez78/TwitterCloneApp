package com.example.twittercloneapp.domain.models

import com.google.gson.annotations.SerializedName

data class Tweet(
    val id: String,
    val userID: String,
    val message: String,
    val date: String
)
