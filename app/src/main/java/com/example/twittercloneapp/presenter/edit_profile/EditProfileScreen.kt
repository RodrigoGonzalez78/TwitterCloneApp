package com.example.twittercloneapp.presenter.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.twittercloneapp.presenter.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    val name by viewModel.name.collectAsState()
    val lastname by viewModel.lastname.collectAsState()
    val dateBirth by viewModel.dateBirth.collectAsState()
    val ubication by viewModel.ubication.collectAsState()
    val website by viewModel.website.collectAsState()
    val bibliography by viewModel.bibliography.collectAsState()
    val messageAlert by viewModel.messageAlert.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Datos") },
                navigationIcon = {
                    IconButton(onClick ={ navController.navigate(Screen.Home.route)}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = lastname,
                onValueChange = { viewModel.updateLastname(it) },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dateBirth,
                onValueChange = { viewModel.updateDateBirth(it) },
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ubication,
                onValueChange = { viewModel.updateUbication(it) },
                label = { Text("Ubication") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = website,
                onValueChange = { viewModel.updateWebsite(it) },
                label = { Text("Website") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = bibliography,
                onValueChange = { viewModel.updateBibliography(it) },
                label = { Text("Bibliography") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.saveProfile() }, modifier = Modifier.fillMaxWidth()) {
                Text("Save")
            }
            Button(onClick = { viewModel.closeSession() }, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar Sesion")
            }
            LaunchedEffect(messageAlert) {
                    Toast.makeText(context, messageAlert, Toast.LENGTH_LONG).show()

            }
        }
    }
}
