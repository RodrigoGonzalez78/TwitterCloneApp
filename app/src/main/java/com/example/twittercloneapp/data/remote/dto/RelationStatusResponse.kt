package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RelationStatusResponse(
    @SerializedName("status") val status: Boolean
)