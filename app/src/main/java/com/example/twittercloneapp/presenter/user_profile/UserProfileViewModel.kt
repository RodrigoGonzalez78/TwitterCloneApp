package com.example.twittercloneapp.presenter.user_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _profileData = MutableStateFlow(UserDto())
    val profileData: StateFlow<UserDto> = _profileData.asStateFlow()

    private val _profileTweets = MutableStateFlow<List<TweetDto>>(emptyList())
    val profileTweets: StateFlow<List<TweetDto>> = _profileTweets.asStateFlow()

    private val _isFollowing = MutableStateFlow<Boolean>(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

    private val _avatarBitmap = mutableStateOf<Bitmap?>(null)
    val avatarBitmap: State<Bitmap?> get() = _avatarBitmap

    private val _bannerBitmap = mutableStateOf<Bitmap?>(null)
    val bannerBitmap: State<Bitmap?> get() = _bannerBitmap

    fun loadUser(userId: String) {
        getProfileData(userId)
        getProfileTweets(userId)
        isFollowing(userId)
        fetchAvatar(userId)
        fetchBanner(userId)
    }


    private fun fetchAvatar(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response = apiService.getAvatar(userId, token)
                val inputStream: InputStream? = response.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _avatarBitmap.value = bitmap
            } catch (e: Exception) {
                Log.d("MYApp", "Erroe al obtener el avatar")
                _avatarBitmap.value = null
            }
        }
    }

    private fun fetchBanner(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response = apiService.getBanner(userId, token)
                val inputStream: InputStream? = response.body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                _bannerBitmap.value = bitmap
            } catch (e: Exception) {
                Log.d("MYApp", "Erroe al obtener el Banner")
                _bannerBitmap.value = null
            }
        }
    }

    fun isFollowing(userId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response = apiService.consultRelation(userId, token)
                _isFollowing.value = response.status
            } catch (e: Exception) {
                Log.e("MiApp", "Error al consultar la realacion" + e.message.toString())
            }
        }
    }

    fun following(userId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                apiService.createRelation(userId, token)
            } catch (e: Exception) {
                Log.e("MiApp", "Error al seguir a un usuario" + e.message.toString())
            }
        }
    }

    fun downFollowing(userId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                apiService.deleteRelation(userId, token)
            } catch (e: Exception) {
                Log.e("MiApp", "Error al dejar de seguir al usuario" + e.message.toString())
            }
        }
    }

    private fun getProfileData(userId: String) {

        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val profileDt = apiService.viewProfile(token, userId)
                _profileData.value = profileDt

            } catch (e: Exception) {
                Log.e("MiApp", "Carar perfil" + e.message.toString())
            }
        }
    }

    private fun getProfileTweets(userId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                _profileTweets.value = apiService.readTweets(id = userId, page = 1, token = token)
            } catch (e: Exception) {
                Log.e("MiApp", "Cargar los tweets del perfil" + e.message.toString())
            }
        }
    }


}