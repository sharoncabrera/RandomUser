package com.challenge.randomuser.presentation.userDetail

import com.challenge.domain.model.User

data class UserDetailUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)