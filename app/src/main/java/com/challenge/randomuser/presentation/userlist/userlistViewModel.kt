package com.challenge.randomuser.presentation.userlist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.model.User
import com.challenge.domain.usecase.DeleteUserUseCase
import com.challenge.domain.usecase.FilterUsersUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val filterUsersUseCase: FilterUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> get() = _uiState

    init {
        loadUsers(3)
    }

    fun loadUsers(count: Int) {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            try {
                getUsersUseCase(count).collect { userList ->
                    _uiState.value = UserListUiState.Success(userList)
                }
            } catch (e: Exception) {
                _uiState.value = UserListUiState.Error("Failed to load users: ${e.message}")
            }
        }
    }

    fun deleteUser(user: User) {
        /* viewModelScope.launch {
             deleteUserUseCase.invoke(user)
         }

         */
    }

    fun filterUsers(query: String) {
        /*  viewModelScope.launch {
              filterUsersUseCase(query).collect { userList ->
                  _uiState.value = UserListUiState.Success(userList)
              }
          }

         */
    }
}
