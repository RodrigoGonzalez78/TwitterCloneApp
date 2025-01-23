package com.example.twittercloneapp.presenter.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twittercloneapp.presenter.home_screen.components.Profile
import com.example.twittercloneapp.presenter.home_screen.components.SearchList
import com.example.twittercloneapp.presenter.home_screen.components.TweetList


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {

    val posts by viewModel.posts.collectAsState()
    val currentScreen = remember { mutableStateOf("TweetList") }

    Scaffold(
        bottomBar = {
            BottomAppBar() {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { currentScreen.value = "TweetList" }) {
                    Icon(Icons.Default.Home, contentDescription = "TweetList")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { currentScreen.value = "Search" }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { currentScreen.value = "Profile" }) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (currentScreen.value) {
                "TweetList" -> TweetList(posts = posts)
                "Search" -> SearchList()
                "Profile" -> Profile()
                else -> TweetList(posts = posts)
            }
        }
    }

}




