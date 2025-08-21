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
class GetUserInfoUseCaseTest {

    @Mock
    private lateinit var mockRepository: UserRepository

    private lateinit var getUserInfoUseCase: GetUserInfoUseCase

    @Before
    fun setUp() {
        getUserInfoUseCase = GetUserInfoUseCase(mockRepository)
    }

    @Test
    fun the_use_case_should_return_a_user_when_the_repository_finds_one() = runTest {

        val expectedUser = User(
            "1", "test@test.com", "Test", "User",
            "", "", "", "", "", "",
            "", "", "", ""
        )

        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(Result.Success(expectedUser)))

        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Success)
        assertEquals(expectedUser, (result as Result.Success).data)
    }

    @Test
    fun the_use_case_should_fail_when_a_user_is_not_found_in_the_DB() = runTest {
        // Preparation
        val errorResult = Result.Error(DataError.Local.USER_NOT_FOUND_IN_DB)
        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(errorResult))

        // Action
        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Local.USER_NOT_FOUND_IN_DB, (result as Result.Error).error)
    }


    @Test
    fun the_use_case_should_handle_database_read_errors_properly() = runTest {
        val errorResult = Result.Error(DataError.Local.DATABASE_READ_ERROR)
        whenever(mockRepository.getUserById(any())).thenReturn(flowOf(errorResult))

        val result = getUserInfoUseCase.invoke("1").first()

        assertTrue(result is Result.Error)
        assertEquals(DataError.Local.DATABASE_READ_ERROR, (result as Result.Error).error)
    }
}