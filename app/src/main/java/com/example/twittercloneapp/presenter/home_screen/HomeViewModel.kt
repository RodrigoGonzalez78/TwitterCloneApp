package com.example.twittercloneapp.presenter.home_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.ReturnTweetsFollowers
import com.example.twittercloneapp.data.remote.dto.TweetDto
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<ReturnTweetsFollowers>>(emptyList())
    val posts: StateFlow<List<ReturnTweetsFollowers>> = _posts.asStateFlow()

    private val _profileData = MutableStateFlow(UserDto())
    val profileData: StateFlow<UserDto> = _profileData.asStateFlow()

    private val _searchTerm = MutableStateFlow<String>("")
    val searchTerm: StateFlow<String> = _searchTerm.asStateFlow()

    private val _searchResult = MutableStateFlow<List<UserDto>>(emptyList())
    val searchResult: StateFlow<List<UserDto>> = _searchResult.asStateFlow()

    private val _profileTweets = MutableStateFlow<List<TweetDto>>(emptyList())
    val profileTweets: StateFlow<List<TweetDto>> = _profileTweets.asStateFlow()

    private val _avatarBitmap = mutableStateOf<Bitmap?>(null)
    val avatarBitmap: State<Bitmap?> get() = _avatarBitmap

    private val _bannerBitmap = mutableStateOf<Bitmap?>(null)
    val bannerBitmap: State<Bitmap?> get() = _bannerBitmap

    private val _avatarUri = mutableStateOf<Uri?>(null)
    private val _bannerUri = mutableStateOf<Uri?>(null)


    init {
        getTweets()
    }


    fun onAvatarSelected(uri: Uri?) {
        _avatarUri.value = uri
    }

    fun onBannerSelected(uri: Uri?) {
        _bannerUri.value = uri
    }

    fun fetchMyAvatar() {
        viewModelScope.launch {
            val idUser = dataStore.getUserId().first().toString()
            _avatarBitmap.value = fetchAvatar(idUser)
        }

    }

    fun fetchMyBanner() {
        viewModelScope.launch {
            val idUser = dataStore.getUserId().first().toString()
            _bannerBitmap.value = fetchBanner(idUser)
        }

    }

    suspend fun fetchBanner(userId: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response = apiService.getBanner(userId, token)

                response.body()?.byteStream()?.use { inputStream ->
                    return@withContext BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.d("MYApp", "Error al obtener el banner: ${e.message}")
                return@withContext null
            }
        }
    }


    suspend fun fetchAvatar(userId: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response = apiService.getAvatar(userId, token)

                response.body()?.byteStream()?.use { inputStream ->
                    return@withContext BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.d("MYApp", "Erroe al obtener el avatar")
                return@withContext null
            }
        }
    }


    fun uploadAvatar(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {

                val file = getFileFromUri(context, uri)
                file?.let {
                    val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("avatar", it.name, requestFile)

                    val token = "Bearer " + dataStore.getJwt().first().toString()
                    val response = apiService.uploadAvatar(body, token)
                    if (response.isSuccessful) {
                        Log.d("Upload", "Avatar subido correctamente")

                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        Log.e("Upload", "Error al subir avatar: $errorMsg")
                    }
                } ?: Log.e("Upload", "No se pudo crear el archivo a partir de la URI")
            } catch (e: Exception) {
                Log.e("Upload", "Excepción al subir avatar: ${e.message}")
            }
        }
    }


    fun uploadBanner(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {

                val file = getFileFromUri(context, uri)
                file?.let {
                    val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("banner", it.name, requestFile)

                    val token = "Bearer " + dataStore.getJwt().first().toString()
                    val response = apiService.uploadBanner(body, token)
                    if (response.isSuccessful) {
                        Log.d("Upload", "Banner subido correctamente")

                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        Log.e("Upload", "Error al subir Banner: $errorMsg")
                    }
                } ?: Log.e("Upload", "No se pudo crear el archivo a partir de la URI")
            } catch (e: Exception) {
                Log.e("Upload", "Excepción al subir banner: ${e.message}")
            }
        }
    }


    private suspend fun getFileFromUri(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = File.createTempFile("upload", ".jpg", context.cacheDir)
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    file
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    fun changeSearchTerm(word: String) {
        _searchTerm.value = word
        searchUsers(_searchTerm.value)
    }



    fun profileData() {
        this.getProfile()
        this.getProfileTweets()
        this.fetchMyAvatar()
        this.fetchMyBanner()
    }

    suspend fun getUserProfile(userId: String): UserDto {
        return try {
            val token = "Bearer " + dataStore.getJwt().first().toString()
            val profileDt = apiService.viewProfile(token, userId)
            return profileDt
        } catch (e: Exception) {
            Log.e("MiApp", "Carar perfil" + e.message.toString())
            return UserDto()
        }
    }


    private fun searchUsers(searchWords: String) {
        viewModelScope.launch {
            try {

                if (searchWords.isEmpty()) {
                    _searchResult.value = emptyList()
                } else {
                    val token = "Bearer " + dataStore.getJwt().first().toString()
                    _searchResult.value =
                        apiService.listUsers(1, "", searchWords, token) ?: emptyList()
                }

            } catch (e: Exception) {
                Log.e("MiApp", "Carar perfil" + e.message.toString())
            }
        }
    }

    suspend fun isFollowing(userId: String): Boolean {
        return try {
            val token = "Bearer " + dataStore.getJwt().first().toString()
            val response = apiService.consultRelation(userId, token)
            response.status
        } catch (e: Exception) {
            Log.e("MiApp", "Error al consultar la relación: ${e.message}")
            false
        }
    }

    private fun getProfile() {

        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val idUser = dataStore.getUserId().first().toString()
                val profileDt = apiService.viewProfile(token, idUser)
                _profileData.value = profileDt

            } catch (e: Exception) {
                Log.e("MiApp", "Carar perfil" + e.message.toString())
            }
        }
    }

    private fun getProfileTweets() {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val id = dataStore.getUserId().first().toString()
                _profileTweets.value = apiService.readTweets(id = id, page = 1, token = token)
            } catch (e: Exception) {
                Log.e("MiApp", "Cargar los tweets del perfil" + e.message.toString())
            }
        }
    }

    private fun getTweets() {
        viewModelScope.launch {
            try {
                val token = "Bearer " + dataStore.getJwt().first().toString()
                val tweets = apiService.readTweetsFollowers(1, token)
                _posts.value = tweets ?: emptyList()

            } catch (e: Exception) {
                Log.e("MiApp", "Cargar los tweets" + e.message.toString())
            }
        }
    }

}