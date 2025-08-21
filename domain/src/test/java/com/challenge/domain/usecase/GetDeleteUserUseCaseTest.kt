package com.challenge.domain.usecase


import com.challenge.domain.model.User
import com.challenge.domain.repository.UserRepository
import com.challenge.domain.util.DataError
import com.challenge.domain.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetDeleteUserUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getDeleteUserUseCase: GetDeleteUserUseCase

    @BeforeEach
    fun setUp() {
        getDeleteUserUseCase = GetDeleteUserUseCase(mockRepository)
    }

    @Test
    fun `the use case should succeed when the user is successfully deleted from the repository`() =
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
    fun `the use case should return an error if the repository fails to delete the user`() =
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