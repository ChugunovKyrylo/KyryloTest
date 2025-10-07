package com.kyrylo.gifs

sealed class DataResult<T> {

    class Empty<T> : DataResult<T>()

    class Received<T>(val data: T) : DataResult<T>()

    class Error<T>(val data: T?, val error: Throwable) : DataResult<T>()

    fun getValueOrNull(): T? {
        return when (this) {
            is Received -> this.data
            is Error -> this.data
            else -> null
        }
    }

}