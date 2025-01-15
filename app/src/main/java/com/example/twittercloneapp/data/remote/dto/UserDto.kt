package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("dateBirth") val dateBirth: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("banner") val banner: String?,
    @SerializedName("bibliography") val bibliography: String?,
    @SerializedName("ubication") val ubication: String?,
    @SerializedName("webSite") val webSite: String?,
    @SerializedName("password") val password: String?
)
