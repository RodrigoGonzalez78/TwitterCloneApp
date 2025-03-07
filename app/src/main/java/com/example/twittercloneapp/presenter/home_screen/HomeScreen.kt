package com.example.twittercloneapp.presenter.home_screen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.twittercloneapp.presenter.home_screen.components.SearchList
import com.example.twittercloneapp.presenter.home_screen.components.TweetList
import com.example.twittercloneapp.presenter.home_screen.components.UserProfile
import com.example.twittercloneapp.presenter.navigation.Screen


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {

    val currentScreen = remember { mutableStateOf("TweetList") }
    val context = LocalContext.current


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
                IconButton(onClick = {
                    viewModel.profileData()
                    currentScreen.value = "Profile"
                }
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        },

        floatingActionButton = {

            when (currentScreen.value) {
                "TweetList" -> FloatingActionButton(onClick = {
                    navController.navigate(Screen.NewTweet.route)
                }) {
                    Icon(imageVector = Icons.Default.Add, "")
                }

                else -> {}
            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (currentScreen.value) {
                "TweetList" -> TweetList(viewModel)
                "Search" -> SearchList(navController, viewModel)
                "Profile" -> UserProfile(
                    viewModel = viewModel,
                    navController = navController
                )

                else -> TweetList(viewModel)
            }
        }
    }

}




