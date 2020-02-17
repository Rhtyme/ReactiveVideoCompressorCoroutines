package com.rhtyme.coroutinevideocompressor.model

sealed class ProgressiveResult<T> {

    class Progress<T>(var progress: Double? = null): ProgressiveResult<T>()
    class Result<T>(var result: T): ProgressiveResult<T>()

}