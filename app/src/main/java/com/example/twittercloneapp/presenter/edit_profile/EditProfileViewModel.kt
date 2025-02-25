package com.example.twittercloneapp.presenter.edit_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import com.example.twittercloneapp.data.remote.dto.UserDto
import com.example.twittercloneapp.data.repository.DataStoreRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastname = MutableStateFlow("")
    val lastname: StateFlow<String> = _lastname.asStateFlow()

    private val _dateBirth = MutableStateFlow("")
    val dateBirth: StateFlow<String> = _dateBirth.asStateFlow()

    private val _ubication = MutableStateFlow("")
    val ubication: StateFlow<String> = _ubication.asStateFlow()

    private val _website = MutableStateFlow("")
    val website: StateFlow<String> = _website.asStateFlow()

    private val _bibliography = MutableStateFlow("")
    val bibliography: StateFlow<String> = _bibliography.asStateFlow()

    private val _messageAlert = MutableStateFlow("")
    val messageAlert: StateFlow<String> = _messageAlert.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {

            try {

                val token = "Bearer " + dataStore.getJwt().first().toString()
                val idUser = dataStore.getUserId().first().toString()
                val user = apiService.viewProfile(token, idUser)

                _name.value = user.name ?: ""
                _lastname.value = user.lastName ?: ""
                _dateBirth.value = user.dateBirth ?: ""
                _ubication.value = user.ubication ?: ""
                _website.value = user.webSite ?: ""
                _bibliography.value = user.bibliography ?: ""

            } catch (e: Exception) {
                _messageAlert.value = e.message.toString()
                Log.e("MiApp","Carar perfil"+ e.message.toString())
                Log.e("MiApp",dataStore.getUserId().first().toString())
            }

        }
    }

    fun updateName(value: String) {
        _name.value = value
    }

    fun updateLastname(value: String) {
        _lastname.value = value
    }

    fun updateDateBirth(value: String) {
        _dateBirth.value = value
    }

    fun updateUbication(value: String) {
        _ubication.value = value
    }

    fun updateWebsite(value: String) {
        _website.value = value
    }

    fun updateBibliography(value: String) {
        _bibliography.value = value
    }

    fun saveProfile() {
        viewModelScope.launch {


            try {
                val user = UserDto(

                   name=  _name.value.ifEmpty { null },
                    lastName =  _lastname.value.ifEmpty { null },
                    dateBirth =  _dateBirth.value.ifEmpty{null},
                    ubication =  _ubication.value.ifEmpty { null },
                    webSite =  _website.value.ifEmpty { null },
                    bibliography =  _bibliography.value.ifEmpty { null }
                )


                val token = "Bearer " + dataStore.getJwt().first().toString()
                val response= apiService.modifyProfile(token, user)

            } catch (e: Exception) {
                _messageAlert.value = e.message.toString()
            }

        }
    }
}