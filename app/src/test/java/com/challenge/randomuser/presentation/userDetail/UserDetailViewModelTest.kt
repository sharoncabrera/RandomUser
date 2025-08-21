package com.challenge.randomuser.presentation.userDetail


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.domain.model.User
import com.challenge.domain.usecase.GetUserInfoUseCase
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockGetUserInfoUseCase: GetUserInfoUseCase

    private lateinit var viewModel: UserDetailViewModel

    private val testUser = User(
        "1", "test@test.com", "Test", "User",
        "", "", "", "", "", "",
        "", "", "", ""
    )

    @Before
    fun setUp() {
        viewModel = UserDetailViewModel(mockGetUserInfoUseCase)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun when_load_user_is_successful_ui_state_updates_correctly() = runTest {
        // Arrange
        whenever(mockGetUserInfoUseCase.invoke(any()))
            .thenReturn(flowOf(Result.Success(testUser)))

        val states = mutableListOf<UserDetailUiState>()


        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.loadUser("1")
        advanceUntilIdle()

        // Assert
        assertTrue(states.size >= 2)

        // Initial state
        val initialState = states[0]
        assertTrue(initialState.isLoading)
        assertNull(initialState.user)
        assertNull(initialState.errorMessage)

        // Final state
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertNotNull(finalState.user)
        assertEquals(testUser, finalState.user)
        assertNull(finalState.errorMessage)

        job.cancel()
    }


    @Test
    fun when_load_user_fails_ui_state_updates_with_error_message() = runTest {
        // Arrange
        whenever(mockGetUserInfoUseCase.invoke(any())).thenReturn(flowOf(Result.Error(DataError.Local.DATABASE_READ_ERROR)))

        val states = mutableListOf<UserDetailUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        // Act
        viewModel.loadUser("1")
        advanceUntilIdle()

        // Assert
        assertEquals(2, states.size)

        // Initial state
        val initialState = states[0]
        assertTrue(initialState.isLoading)
        assertNull(initialState.user)

        // Final state
        val finalState = states[1]
        assertTrue(!finalState.isLoading)
        assertNull(finalState.user)
        assertEquals("Error loading user", finalState.errorMessage)

        job.cancel()
    }
}
