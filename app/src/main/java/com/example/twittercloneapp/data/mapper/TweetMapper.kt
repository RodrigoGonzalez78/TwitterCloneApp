package com.example.twittercloneapp.data.mapper

import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.domain.models.Tweet

fun Tweet.toDto(): TweetDto {
    return TweetDto(
        id = this.id,
        userID = this.userID,
        message = this.message,
        date = this.date
    )
}

fun TweetDto.toDomain(): Tweet {
    return Tweet(
        id = this.id.orEmpty(),
        userID = this.userID.orEmpty(),
        message = this.message.orEmpty(),
        date = this.date.orEmpty()
    )
}
