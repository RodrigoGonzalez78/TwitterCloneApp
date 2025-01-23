package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReturnTweetsFollowers (
    @SerializedName("_id") val id: String,
    @SerializedName("userId") val userID: String,
    @SerializedName("userRelationId")  val userRelationID: String,
    @SerializedName("tweet") val tweet: TweetDto
)