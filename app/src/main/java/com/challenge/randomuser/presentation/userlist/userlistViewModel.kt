package com.challenge.randomuser.presentation.userlist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.model.User
import com.challenge.domain.usecase.GetDeleteUserUseCase
import com.challenge.domain.usecase.GetFilterUsersUseCase
import com.challenge.domain.usecase.GetLocalUsersUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getLocalUsersUseCase: GetLocalUsersUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getDeleteUserUseCase: GetDeleteUserUseCase,
    private val getFilterUsersUseCase: GetFilterUsersUseCase
) : ViewModel() {

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
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        try {
            getLocalUsersUseCase().collect { initialUsers ->
                _uiState.value = _uiState.value.copy(
                    users = getNonDeletedUsers(initialUsers),
                    allUsersCache = initialUsers,
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
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        try {
            getUsersUseCase(count).collect { newUsers ->
                val existingUsers = _uiState.value.allUsersCache
                val combinedUsers = (existingUsers + newUsers).distinctBy { it.id }
                _uiState.value = _uiState.value.copy(
                    users = getNonDeletedUsers(combinedUsers),
                    allUsersCache = combinedUsers,
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

            val updatedCache = _uiState.value.allUsersCache.map {
                if (it.id == userToDelete.id) it.copy(isDeleted = true) else it
            }
            _uiState.value = _uiState.value.copy(
                users = getNonDeletedUsers(updatedCache),
                allUsersCache = updatedCache
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to delete user: ${e.message}"
            )
        }
    }

    private suspend fun handleFilterUsers(query: String) {
        _uiState.value =
            _uiState.value.copy(searchQuery = query, isLoading = true, errorMessage = null)
        try {
            getFilterUsersUseCase(query, _uiState.value.allUsersCache).collect { filteredUsers ->
                _uiState.value = _uiState.value.copy(
                    users = getNonDeletedUsers(filteredUsers),
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to filter users: ${e.message}"
            )
        }
    }

    private fun getNonDeletedUsers(users: List<User>): List<User> {
        return users.filter { !it.isDeleted }
    }

    companion object {
        private const val INITIAL_USER_COUNT = 1
    }
}
