package com.challenge.randomuser.presentation.userlist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.model.User
import com.challenge.domain.usecase.GetDeleteUserUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import com.challenge.domain.util.DataError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.challenge.domain.util.Result


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
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        getUsersUseCase(INITIAL_USER_COUNT, true).collect { result ->
            when (result) {
                is Result.Success -> {
                    allUsersCache = result.data
                    _uiState.value = _uiState.value.copy(
                        users = getPresentableUsers(_uiState.value.searchQuery),
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = mapError(result.error)
                    )
                }
            }
        }
    }

    private suspend fun handleFetchMoreUsers(count: Int) {
        if (_uiState.value.isLoading) return
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        getUsersUseCase(count, false).collect { result ->
            when (result) {
                is Result.Success -> {
                    allUsersCache = (allUsersCache + result.data).distinctBy { it.id }
                    _uiState.value = _uiState.value.copy(
                        users = getPresentableUsers(_uiState.value.searchQuery),
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = mapError(result.error)
                    )
                }
            }
        }
    }

    private suspend fun handleDeleteUser(userToDelete: User) {
        when (val result = getDeleteUserUseCase(userToDelete)) {
            is Result.Success -> {
                allUsersCache = allUsersCache.filter { it.id != userToDelete.id }
                _uiState.value = _uiState.value.copy(
                    users = getPresentableUsers(_uiState.value.searchQuery)
                )
            }

            is Result.Error -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = mapError(result.error)
                )
            }
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

    private fun mapError(error: DataError): String {
        return when (error) {
            DataError.Network.NO_INTERNET_CONNECTION -> "No internet connection"
            DataError.Network.SERVER_ERROR -> "Server error"
            DataError.Network.REQUEST_TIMEOUT -> "Request timed out"
            DataError.Network.SERIALIZATION_ERROR -> "Serialization error"
            DataError.Network.UNKNOWN -> "Unknown network error"

            DataError.Local.DISK_FULL -> "Disk is full"
            DataError.Local.DATABASE_READ_ERROR -> "Ops! Something went wrong"
            DataError.Local.DATABASE_WRITE_ERROR -> "Failed to delete user"
            DataError.Local.USER_NOT_FOUND_IN_DB -> "User not found"
            DataError.Local.UNKNOWN -> "Unknown local error"
        }
    }


    companion object {
        private const val INITIAL_USER_COUNT = 1
    }
}
