package com.challenge.randomuser.presentation

import UserDetailScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.challenge.randomuser.presentation.userDetail.UserDetailViewModel
import com.challenge.randomuser.presentation.userlist.UserListScreen
import com.challenge.randomuser.presentation.userlist.UserListViewModel

sealed class Screen(val route: String) {
    object UserList : Screen("user_list")
    object UserDetail : Screen("user_detail/{userId}") {
        fun createRoute(userId: String) = "user_detail/$userId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.UserList.route) {
        composable(Screen.UserList.route) {
            val viewModel: UserListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            UserListScreen(
                uiState = uiState,
                onUserClick = { userId ->
                    navController.navigate(Screen.UserDetail.createRoute(userId))
                },
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: UserDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            val userId = backStackEntry.arguments?.getString("userId")
            LaunchedEffect(userId) {
                if (userId != null) {
                    viewModel.loadUser(userId)
                }
            }
            UserDetailScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}