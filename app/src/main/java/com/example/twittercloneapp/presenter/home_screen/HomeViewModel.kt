package com.example.twittercloneapp.presenter.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twittercloneapp.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
) :ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        viewModelScope.launch {
            _posts.value = getSamplePosts()
        }
    }

    private fun getSamplePosts() = listOf(
        Post(
            id = "1",
            authorName = "Tech User",
            authorHandle = "@techuser",
            authorAvatar = "https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Captura%20desde%202025-01-18%2001-05-28-c5QsoemRJheVEmp6EbgSeqEIz0DRCJ.png",
            content = "Just finished an amazing presentation at the tech conference! The future of AI is incredibly exciting. Thanks everyone for the great questions and engagement! #TechConf2024 #AI",
            timeAgo = "2h",
            comments = 24,
            reposts = 142,
            likes = 897
        ),
    )
}

data class Post(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val content: String,
    val timeAgo: String,
    val comments: Int,
    val reposts: Int,
    val likes: Int
)