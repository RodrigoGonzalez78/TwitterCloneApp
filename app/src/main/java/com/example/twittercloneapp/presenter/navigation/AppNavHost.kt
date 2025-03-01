package com.example.twittercloneapp.presenter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.twittercloneapp.presenter.edit_profile.EditUserScreen
import com.example.twittercloneapp.presenter.home_screen.HomeScreen
import com.example.twittercloneapp.presenter.login_screen.LoginScreen
import com.example.twittercloneapp.presenter.new_tweet.NewTweet
import com.example.twittercloneapp.presenter.signup_screen.SignupScreen
import com.example.twittercloneapp.presenter.splash_screen.SplashScreen
import com.example.twittercloneapp.presenter.user_profile.UserProfileScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Signup : Screen("signup")
    data object Login : Screen("login")
    data object NewTweet : Screen("mew_tweet")
    data object EditProfile : Screen("edit_profile")
    data object UsersProfiles : Screen("users_profiles?userId={userId}") {
        fun createRoute(userId: String) = "users_profiles?userId=$userId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        val route = if (isAuthenticated) Screen.Home.route else Screen.Login.route
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen()
        }
        composable(Screen.NewTweet.route) {
            NewTweet(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        composable(Screen.UsersProfiles.route, arguments = listOf(
            navArgument("userId") {
                type = NavType.StringType
                nullable = true
            }
        )) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserProfileScreen(
                navController,
                userId = userId,
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.EditProfile.route) {
            EditUserScreen(navController)
        }
    }
}