package com.example.twittercloneapp.presenter.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun SearchList(
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
            items(searchResults) { users ->
                users.name?.let { Text(it) }
            }
        }
    }
}



