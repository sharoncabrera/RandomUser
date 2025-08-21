package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetUsersUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    private val expectedUsers = listOf(
        User(
            "1", "test@test.com", "Test", "User",
            "", "", "", "", "", "",
            "", "", "", ""
        ),
        User(
            "2", "jane@test.com", "Jane", "Doe",
            "", "", "", "", "", "",
            "", "", "", ""
        )
    )

    @Before
    fun setUp() {
        getUsersUseCase = GetUsersUseCase(mockRepository)
    }

    @Test
    fun the_use_case_should_fetch_users_successfully_on_an_initial_load() = runTest {
        // Simulation
        val successfulUserFlow = flowOf(Result.Success(expectedUsers))
        whenever(mockRepository.getUsers(any(), any())).thenReturn(successfulUserFlow)

        // Action
        val result = getUsersUseCase(count = 10, initial = true).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedUsers, (result as Result.Success).data)
    }

    @Test
    fun the_use_case_should_fail_with_a_network_error_if_the_repository_is_offline() = runTest {

        val errorResult = Result.Error(DataError.Network.NO_INTERNET_CONNECTION)
        val flowOfError = flowOf(errorResult)

        whenever(mockRepository.getUsers(any(), any())).thenReturn(flowOfError)

        val resultFlow = getUsersUseCase(count = 10, initial = true)
        val result = resultFlow.first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NO_INTERNET_CONNECTION, (result as Result.Error).error)
    }
}
