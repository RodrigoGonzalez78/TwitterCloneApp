package com.example.twittercloneapp.presenter.home_screen.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.twittercloneapp.presenter.home_screen.HomeViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twittercloneapp.presenter.navigation.Screen

@Composable
fun SearchList(
    navController: NavController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    val searchResults by viewModel.searchResult.collectAsState()
    val termSearch by viewModel.searchTerm.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = termSearch,
            onValueChange = { viewModel.changeSearchTerm(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Buscar...") },

            singleLine = true
        )


        LazyColumn {
            items(searchResults) { user ->

                var isFollowing by remember { mutableStateOf(false) }
                var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }


                LaunchedEffect(user.id) {
                    isFollowing = viewModel.isFollowing(user.id ?: "")
                    avatarBitmap = viewModel.fetchAvatar(user.id ?: "")
                }

                ProfileCard(
                    name = (user.name ?: "") + " " + (user.lastName ?: ""),
                    description = if (isFollowing) "Seguido" else "No Seguido",
                    onFollowClick = {
                        navController.navigate(
                            Screen.UsersProfiles.createRoute(
                                userId = user.id ?: ""
                            )
                        )
                    },
                    avatarBitmap = avatarBitmap
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    description: String,
    avatarBitmap: Bitmap?,
    onFollowClick: () -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onFollowClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {


        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            avatarBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color.Gray,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(4.dp))

            }

            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}


