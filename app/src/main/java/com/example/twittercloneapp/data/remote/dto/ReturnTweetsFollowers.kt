package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReturnTweetsFollowers (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("userId") val userID: String? = null,
    @SerializedName("userRelationId")  val userRelationID: String? = null,
    @SerializedName("Tweet") val tweet: TweetDto? = null
)