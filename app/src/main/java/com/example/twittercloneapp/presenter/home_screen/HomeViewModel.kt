package com.example.twittercloneapp.presenter.home_screen

import androidx.lifecycle.ViewModel
import com.example.twittercloneapp.data.remote.ApiService
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
) :ViewModel() {
}