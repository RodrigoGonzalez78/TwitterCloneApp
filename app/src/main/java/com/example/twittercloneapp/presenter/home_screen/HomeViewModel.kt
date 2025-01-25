package com.example.twittercloneapp.presenter.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.ReturnTweetsFollowers
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) :ViewModel() {

    private val _posts = MutableStateFlow<List<ReturnTweetsFollowers>>(emptyList())
    val posts: StateFlow<List<ReturnTweetsFollowers>> = _posts.asStateFlow()

    private val _messageAlert = MutableStateFlow("")
    val messageAlert: StateFlow<String> = _messageAlert.asStateFlow()

    private val _profileData = MutableStateFlow(UserDto())
    val profileData: StateFlow<UserDto> =_profileData.asStateFlow()

    init {
        getTweets()
    }

    private fun getProfile(){

        viewModelScope.launch {
            try{
                val token ="Bearer " + dataStore.getJwt().first().toString()
                val idUser=dataStore.getUserId().first().toString()
                val profileDt = apiService.viewProfile(token,idUser)
                _profileData.value=profileDt

            }catch (e: Exception){
                _messageAlert.update {
                    "Error al cargar los tweets"
                }
            }
        }
    }

    private fun getTweets(){
        viewModelScope.launch {
            try {
                val token ="Bearer " + dataStore.getJwt().first().toString()
                val tweets = apiService.readTweetsFollowers(1,token)
                _posts.value = tweets

            }catch (e:Exception){
                _messageAlert.update {
                    "Error al cargar los tweets"
                }
            }
        }
    }
}