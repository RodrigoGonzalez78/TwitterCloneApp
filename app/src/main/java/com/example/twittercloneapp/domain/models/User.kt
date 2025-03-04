package com.example.twittercloneapp.domain.models


data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val dateBirth: String,
    val email: String,
    val avatar: String,
    val banner: String,
    val bibliography: String,
    val ubication: String,
    val webSite: String,
    val password: String
)
