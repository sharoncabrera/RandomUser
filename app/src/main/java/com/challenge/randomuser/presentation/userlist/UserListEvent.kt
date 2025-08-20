package com.challenge.randomuser.presentation.userlist

import com.challenge.domain.model.User

sealed class UserListEvent {
    object LoadInitialUsers : UserListEvent()
    object FetchMoreUsers : UserListEvent()
    data class DeleteUser(val user: User) : UserListEvent()
    data class FilterUsers(val query: String) : UserListEvent()
}