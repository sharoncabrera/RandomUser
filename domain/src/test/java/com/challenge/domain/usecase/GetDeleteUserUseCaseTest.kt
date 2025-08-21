package com.challenge.domain.usecase

import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
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
class GetDeleteUserUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getDeleteUserUseCase: GetDeleteUserUseCase

    @Before
    fun setUp() {
        getDeleteUserUseCase = GetDeleteUserUseCase(mockRepository)
    }

    @Test
    fun the_use_case_should_succeed_when_the_user_is_successfully_deleted_from_the_repository() =
        runTest {

            // Arrange
            whenever(mockRepository.deleteUser(any())).thenReturn(Result.Success(Unit))

            val userToDelete = User(
                "1", "test@test.com", "Test", "User",
                "", "", "", "", "", "",
                "", "", "", ""
            )
            // Action
            val result = getDeleteUserUseCase.invoke(userToDelete)

            assertTrue(result is Result.Success)
            assertEquals(Unit, (result as Result.Success).data)
        }

    @Test
    fun the_use_case_should_return_an_error_if_the_repository_fails_to_delete_the_user() =
        runTest {
            // Arrange
            whenever(mockRepository.deleteUser(any())).thenReturn(Result.Error(DataError.Local.DATABASE_WRITE_ERROR))

            // Action
            val userToDelete = User(
                "1", "test@test.com", "Test", "User",
                "", "", "", "", "", "",
                "", "", "", ""
            )
            val result = getDeleteUserUseCase.invoke(userToDelete)

            assertTrue(result is Result.Error)
            assertEquals(DataError.Local.DATABASE_WRITE_ERROR, (result as Result.Error).error)
        }
}
