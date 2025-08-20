package com.challenge.randomuser.presentation.userlist


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.model.User
import com.challenge.domain.usecase.GetDeleteUserUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getDeleteUserUseCase: GetDeleteUserUseCase,
) : ViewModel() {

    private var allUsersCache = listOf<User>()

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    init {
        onEvent(UserListEvent.LoadInitialUsers)
    }

    // UI interactions
    fun onEvent(event: UserListEvent) {
        viewModelScope.launch {
            when (event) {
                is UserListEvent.LoadInitialUsers -> handleLoadInitialUsers()
                is UserListEvent.FetchMoreUsers -> handleFetchMoreUsers(INITIAL_USER_COUNT)
                is UserListEvent.DeleteUser -> handleDeleteUser(event.user)
                is UserListEvent.FilterUsers -> handleFilterUsers(event.query)
            }
        }
    }

    private suspend fun handleLoadInitialUsers() {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        try {
            getUsersUseCase(INITIAL_USER_COUNT, true).collect { initialUsers ->
                allUsersCache = initialUsers
                _uiState.value = _uiState.value.copy(
                    users = getPresentableUsers(_uiState.value.searchQuery),
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load local users: ${e.message}"
            )
        }
    }

    private suspend fun handleFetchMoreUsers(count: Int) {
        if (_uiState.value.isLoading) return
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        try {
            getUsersUseCase(count, false).collect { newUsers ->
                allUsersCache = (allUsersCache + newUsers).distinctBy { it.id }

                _uiState.value = _uiState.value.copy(
                    users = getPresentableUsers(_uiState.value.searchQuery),
                    isLoading = false,
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load more users: ${e.message}"
            )
        }
    }

    private suspend fun handleDeleteUser(userToDelete: User) {
        try {
            getDeleteUserUseCase(userToDelete)
            allUsersCache = allUsersCache.filter { it.id != userToDelete.id }

            _uiState.value = _uiState.value.copy(
                users = getPresentableUsers(_uiState.value.searchQuery)
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to delete user: ${e.message}"
            )
        }
    }

    private suspend fun handleFilterUsers(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            isLoading = true,
            errorMessage = null
        )
        try {
            _uiState.value = _uiState.value.copy(
                users = getPresentableUsers(query),
                isLoading = false
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to filter users: ${e.message}"
            )
        }
    }

    private fun getPresentableUsers(query: String): List<User> {
        return allUsersCache.filter { user ->
            (user.name.contains(query, ignoreCase = true) ||
                    user.lastName.contains(query, ignoreCase = true) ||
                    user.email.contains(query, ignoreCase = true))
                    && !user.isDeleted
        }
    }

    companion object {
        private const val INITIAL_USER_COUNT = 1
    }
}
