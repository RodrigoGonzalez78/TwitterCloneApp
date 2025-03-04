package com.example.twittercloneapp.data.mapper

import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.domain.models.User

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        lastName = this.lastName,
        dateBirth = this.dateBirth,
        email = this.email,
        avatar = this.avatar,
        banner = this.banner,
        bibliography = this.bibliography,
        ubication = this.ubication,
        webSite = this.webSite,
        password = this.password
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        lastName = this.lastName.orEmpty(),
        dateBirth = this.dateBirth.orEmpty(),
        email = this.email.orEmpty(),
        avatar = this.avatar.orEmpty(),
        banner = this.banner.orEmpty(),
        bibliography = this.bibliography.orEmpty(),
        ubication = this.ubication.orEmpty(),
        webSite = this.webSite.orEmpty(),
        password = this.password.orEmpty()
    )
}