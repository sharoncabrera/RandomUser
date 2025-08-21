package com.challenge.domain.util

sealed interface DataError : Error {
    //possible errors on network and database
    enum class Network : DataError {
        NO_INTERNET_CONNECTION,
        SERVER_ERROR,
        REQUEST_TIMEOUT,
        SERIALIZATION_ERROR,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        DATABASE_READ_ERROR,
        DATABASE_WRITE_ERROR,
        USER_NOT_FOUND_IN_DB,
        UNKNOWN

    }
}