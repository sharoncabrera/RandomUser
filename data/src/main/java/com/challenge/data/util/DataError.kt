package com.challenge.data.util

sealed interface DataError: Error {
    //possible errors on network and database
    enum class Network: DataError{
        NO_INTERNET_CONNECTION,
        SERVER_ERROR,
        REQUEST_TIMEOUT,
        SERIALIZATION_ERROR,
        UNKNOWN
    }
    enum class Local: DataError{
        DISK_FULL, //no enough storage on the device
    }
}