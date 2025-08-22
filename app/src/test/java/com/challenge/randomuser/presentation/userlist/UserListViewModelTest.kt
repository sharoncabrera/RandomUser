package com.challenge.randomuser.presentation.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.domain.model.User
import com.challenge.domain.usecase.GetDeleteUserUseCase
import com.challenge.domain.usecase.GetUsersUseCase
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockGetUsersUseCase: GetUsersUseCase

    @Mock
    private lateinit var mockGetDeleteUserUseCase: GetDeleteUserUseCase

    private lateinit var viewModel: UserListViewModel

    private val user1 =
        User("1", "a@test.com", "test", "test", "", "", "", "", "", "", "", "", "", "")
    private val user2 =
        User("2", "b@test.com", "Jane", "Doe", "", "", "", "", "", "", "", "", "", "")
    private val user3 =
        User("3", "c@test.com", "Sharon", "Test", "", "", "", "", "", "", "", "", "", "")


    private val initialUsers = listOf(user1, user2)
    private val additionalUser = listOf(user3)


    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = UserListViewModel(mockGetUsersUseCase, mockGetDeleteUserUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearInvocations(mockGetUsersUseCase, mockGetDeleteUserUseCase)
        Mockito.reset(mockGetUsersUseCase, mockGetDeleteUserUseCase)

    }

    @Test
    fun when_load_initial_users_is_successful_ui_state_updates_correctly() = runTest {
        // Arrange
        whenever(mockGetUsersUseCase.invoke(any(), any())).thenReturn(
            flowOf(
                Result.Success(
                    initialUsers
                )
            )
        )

        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        // Assert
        assertEquals(2, states.size)


        val finalState = states[1]
        assertFalse(finalState.isLoading)
        assertEquals(initialUsers, finalState.users)

        job.cancel()

    }

    @Test
    fun when_fetching_more_users_with_success_ui_state_updates_correctly() = runTest {
        // Arrange
        whenever(mockGetUsersUseCase.invoke(any(), any()))
            .thenReturn(flowOf(Result.Success(initialUsers)))
            .thenReturn(flowOf(Result.Success(listOf(user3))))

        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        viewModel.onEvent(UserListEvent.FetchMoreUsers)
        advanceUntilIdle()

        // Assert
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertEquals(3, finalState.users.size)
        assertEquals(listOf(user1, user2, user3), finalState.users)

        job.cancel()
    }

    @Test
    fun when_initial_load_fails_ui_state_shows_error_message() = runTest {
        // Arrange
        whenever(mockGetUsersUseCase.invoke(any(), any()))
            .thenReturn(flowOf(Result.Error(DataError.Network.NO_INTERNET_CONNECTION)))

        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        // Assert
        assertEquals(2, states.size)


        val finalState = states[1]
        assertFalse(finalState.isLoading)
        assertEquals("No internet connection", finalState.errorMessage)

        job.cancel()
    }

    @Test
    fun when_fetching_more_users_handles_duplicates() = runTest {

        whenever(mockGetUsersUseCase.invoke(any(), any()))
            .thenReturn(flowOf(Result.Success(initialUsers))) // first call (init)
            .thenReturn(flowOf(Result.Success(additionalUser))) // second call (fetchMore)
            .thenReturn(flowOf(Result.Success(additionalUser))) // duplicated user (fetchMore)


        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        viewModel.onEvent(UserListEvent.FetchMoreUsers)
        advanceUntilIdle()

        viewModel.onEvent(UserListEvent.FetchMoreUsers)
        advanceUntilIdle()

        // Assert

        assertEquals(3, states.size)


        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertEquals(3, finalState.users.size)
        assertEquals(listOf(user1, user2, user3), finalState.users)

        job.cancel()

    }


    @Test
    fun when_deleting_user_it_is_removed_from_ui_and_cache() = runTest {
        // Arrange
        whenever(mockGetUsersUseCase.invoke(any(), any()))
            .thenReturn(flowOf(Result.Success(initialUsers)))

        whenever(mockGetDeleteUserUseCase.invoke(any()))
            .thenReturn(Result.Success(Unit))

        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        // Act
        viewModel.onEvent(UserListEvent.DeleteUser(user1))
        advanceUntilIdle()

        // Assert
        val finalState = states.last()
        assertEquals(1, finalState.users.size)
        assertEquals(listOf(user2), finalState.users)

        job.cancel()
    }

    @Test
    fun when_filtering_users_ui_state_updates_correctly() = runTest {
        // Arrange
        whenever(mockGetUsersUseCase.invoke(any(), any()))
            .thenReturn(flowOf(Result.Success(initialUsers + additionalUser)))

        val states = mutableListOf<UserListUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }
        viewModel.onEvent(UserListEvent.LoadInitialUsers)
        advanceUntilIdle()

        // Act
        viewModel.onEvent(UserListEvent.FilterUsers("sharon"))
        advanceUntilIdle()

        // Assert
        val finalState = states.last()
        assertEquals(1, finalState.users.size)
        assertEquals(user3, finalState.users.first())

        job.cancel()
    }
}

