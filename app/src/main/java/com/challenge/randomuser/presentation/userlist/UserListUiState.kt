package com.challenge.randomuser.presentation.userlist

import com.challenge.domain.model.User

data class UserListUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,
    val allUsersCache: List<User> = emptyList(),
    val searchQuery: String = ""
)

