package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import org.mockito.Mock
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


@ExtendWith(MockitoExtension::class)
class GetUsersUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @BeforeEach
    fun setUp() {
        getUsersUseCase = GetUsersUseCase(mockRepository)
    }

    @Test
    fun `the use case should fetch users successfully on an initial load`() = runTest {
        //Simulation
        val successfulUserFlow = flowOf(Result.Success(expectedUsers))
        whenever(mockRepository.getUsers(any(), any())).thenReturn(successfulUserFlow)

        // Action
        val result = getUsersUseCase(count = 10, initial = true).first()

        assertTrue(result is Result.Success)
        assertEquals(expectedUsers, (result as Result.Success).data)
    }

    @Test
    fun `the use case should fail with a network error if the repository is offline`() = runTest {

        val errorResult = Result.Error(DataError.Network.NO_INTERNET_CONNECTION)
        val flowOfError = flowOf(errorResult)

        whenever(mockRepository.getUsers(any(), any())).thenReturn(flowOfError)


        val resultFlow = getUsersUseCase(count = 10, initial = true)
        val result = resultFlow.first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NO_INTERNET_CONNECTION, (result as Result.Error).error)
    }
}


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