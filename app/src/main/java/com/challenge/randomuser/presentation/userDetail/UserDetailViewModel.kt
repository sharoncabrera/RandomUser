package com.challenge.randomuser.presentation.userDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.challenge.domain.util.Result

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailUiState(isLoading = true))
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()


    fun loadUser(userId: String) {
        viewModelScope.launch {
            getUserUseCase(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Error loading user"
                        )
                    }
                }
            }
        }
    }

}