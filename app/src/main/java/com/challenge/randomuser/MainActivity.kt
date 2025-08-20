package com.challenge.randomuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenge.randomuser.presentation.userlist.UserListScreen
import com.challenge.randomuser.presentation.userlist.UserListViewModel
import com.challenge.randomuser.ui.theme.RandomUserTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: UserListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: UserListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            RandomUserTheme {
                UserListScreen(
                    uiState = uiState,
                    // onLoadMore = { viewModel.loadMore() },
                    onDeleteUser = { user -> viewModel.deleteUser(user) },
                    onFilterUsers = { query -> viewModel.filterUsers(query) }
                )
            }
        }
    }
}