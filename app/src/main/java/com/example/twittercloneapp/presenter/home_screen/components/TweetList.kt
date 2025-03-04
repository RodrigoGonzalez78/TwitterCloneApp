package com.example.twittercloneapp.presenter.home_screen.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.presenter.general_components.PostItem
import com.example.twittercloneapp.presenter.home_screen.HomeViewModel


@Composable
fun TweetList(viewModel: HomeViewModel) {
    val posts by viewModel.posts.collectAsState()

    LazyColumn {
        items(posts) { post ->
            var userData by remember { mutableStateOf(UserDto()) }

            LaunchedEffect(post.userID) {
                userData = viewModel.getUserProfile(post.userID ?: "")
            }
            PostItem(post.tweet ?: TweetDto(), userData)
            HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}






