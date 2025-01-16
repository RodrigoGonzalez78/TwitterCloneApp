package com.example.twittercloneapp.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("dateBirth") val dateBirth: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("banner") val banner: String? = null,
    @SerializedName("bibliography") val bibliography: String? = null,
    @SerializedName("ubication") val ubication: String? = null,
    @SerializedName("webSite") val webSite: String? = null,
    @SerializedName("password") val password: String? = null
)
