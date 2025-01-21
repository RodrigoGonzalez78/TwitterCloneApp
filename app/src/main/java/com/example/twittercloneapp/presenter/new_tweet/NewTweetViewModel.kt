package com.example.twittercloneapp.presenter.new_tweet

import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.TweetDto
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
class NewTweetViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewTweetUiState())
    val uiState: StateFlow<NewTweetUiState> = _uiState.asStateFlow()


    private fun clearFields(){
        this.onTweetChange("")
    }

    fun changeNotification( state:Boolean){
        _uiState.update { it.copy(notification = state) }
    }

    fun onTweetChange(newTweet: String) {
        _uiState.update { it.copy(tweet = newTweet) }
    }



    fun validateAndRegister() {
        val currentState = _uiState.value
        val errors = NewTweetUiState(
            tweetError = if ( currentState.tweet.isEmpty()) "" else "",
        )
        _uiState.update {
            it.copy(
                tweetError = errors.tweetError,
            )
        }

        if (errors.hasErrors()) return

        register()
    }

    private fun register() {
        viewModelScope.launch {
            try {

                val tweet = TweetDto(
                    message = _uiState.value.tweet,
                )

                val token ="Bearer " + dataStore.getJwt().first().toString()
                apiService.newTweet(token,tweet)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notification = true,
                        message = "Se creo existosamente el tweet"
                    )
                }
                clearFields()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notification = true,
                        message = "Error al crear el tweet: ${e.message}"
                    )
                }
            }
        }
    }
}

data class NewTweetUiState(
    val tweet: String = "",
    val isLoading: Boolean = false,
    val notification: Boolean = false,
    val message: String = "",
    val tweetError: String = "",
) {
    fun hasErrors() = tweetError.isNotEmpty()
}