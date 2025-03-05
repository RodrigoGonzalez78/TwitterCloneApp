package com.example.twittercloneapp.presenter.home_screen.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.twittercloneapp.presenter.general_components.PostItem
import com.example.twittercloneapp.presenter.home_screen.HomeViewModel
import com.example.twittercloneapp.presenter.navigation.Screen
import com.example.twittercloneapp.utils.Utils


@Composable
fun UserProfile(viewModel: HomeViewModel = hiltViewModel(), navController: NavController) {

    val user by viewModel.profileData.collectAsState()
    val tweetsProfile by viewModel.profileTweets.collectAsState()


    val context = LocalContext.current
    val avatarUri by viewModel.avatarUri

    // Launcher para seleccionar la imagen de la galería.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.onAvatarSelected(it)
            // Opcional: iniciar la subida inmediatamente al seleccionar la imagen.
            viewModel.uploadAvatar(context, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            // Caja para el avatar que es clickable para cambiarlo.
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .offset(x = 16.dp, y = 80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, Color.LightGray, CircleShape)
                    .clickable { launcher.launch("image/*") }
            ) {
                // Si no hay imagen seleccionada ni avatar del usuario, muestra un ícono.
                if (avatarUri == null && user.avatar.isNullOrEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(55.dp)
                            .align(Alignment.Center)
                    )
                } else {
                    // Muestra la imagen seleccionada o la existente del usuario.
                    val painter = if (avatarUri != null) {
                        rememberAsyncImagePainter(avatarUri)
                    } else {
                        rememberAsyncImagePainter(user.avatar)
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Botón para editar el perfil (navega a otra pantalla).
            Button(
                onClick = { navController.navigate(Screen.EditProfile.route) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                border = BorderStroke(1.dp, Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Edit profile")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp)
        ) {
            Text(
                text = "${user.name} ${user.lastName}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = user.bibliography ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ubication",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = user.ubication ?: "",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Website",
                    tint = Color(0xFF1DA1F2),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = user.webSite ?: "",
                    color = Color(0xFF1DA1F2),
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Join date",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = Utils.formatDateLegacy(user.dateBirth),
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)
        LazyColumn {
            items(tweetsProfile) { tweet ->
                PostItem(tweet, user)
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}



