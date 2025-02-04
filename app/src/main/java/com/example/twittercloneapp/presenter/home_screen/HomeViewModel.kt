package com.example.twittercloneapp.presenter.home_screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.*
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.ReturnTweetsFollowers
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.data.repository.DataStoreRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<ReturnTweetsFollowers>>(emptyList())
    val posts: StateFlow<List<ReturnTweetsFollowers>> = _posts.asStateFlow()

    private val _messageAlert = MutableStateFlow("")
    val messageAlert: StateFlow<String> = _messageAlert.asStateFlow()

    private val _profileData = MutableStateFlow(UserDto())
    val profileData: StateFlow<UserDto> = _profileData.asStateFlow()

    init {
        getTweets()
    }


    fun getProfile() {

        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val idUser = dataStore.getUserId().first().toString()
                val profileDt = apiService.viewProfile(token, idUser)
                _profileData.value = profileDt

            } catch (e: Exception) {
                _messageAlert.update {
                   "Carar perfil"+ e.message.toString()
                }
                Log.e("MiApp","Carar perfil"+ e.message.toString())
            }
        }
    }

    private fun getTweets() {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val tweets = apiService.readTweetsFollowers(1, token)
                _posts.value = tweets

            } catch (e: Exception) {
                _messageAlert.update {
                    "Cargar los tweets" + e.message.toString()
                }
                Log.e("MiApp","Cargar los tweets" + e.message.toString())
            }
        }
    }


    fun selectImageFromGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI)
        "image/*".also { intent.type = it }
        launcher.launch(intent)
    }

    fun uploadImage(
        uri: Uri,
        isAvatar: Boolean,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {

        val context =context.applicationContext
        val filePath = getPathFromUri(context, uri)

        if (filePath != null) {
            val file = File(filePath)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                if (isAvatar) "avatar" else "banner",
                file.name,
                requestBody
            )

            viewModelScope.launch {
                try {
                    if (isAvatar) {
                        apiService.uploadAvatar(multipartBody)
                    } else {
                        apiService.uploadBanner(multipartBody)
                    }
                    onSuccess()
                } catch (e: Exception) {
                    onError(e)
                }
            }
        } else {
            onError(IllegalArgumentException("Invalid file path"))
        }
    }

    private fun getPathFromUri(context: android.content.Context, uri: Uri): String? {
        val projection = arrayOf(Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }
}