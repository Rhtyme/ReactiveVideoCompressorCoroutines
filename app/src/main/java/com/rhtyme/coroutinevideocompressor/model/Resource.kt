package com.rhtyme.coroutinevideocompressor.model

import android.content.Context
import com.rhtyme.coroutinevideocompressor.R

sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val errorCode: Int? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)

    /**
     * progress between 0 and 1
     * */
    class Loading<T>(var progress: Double? = null) : Resource<T>()

    class Error<T>(errorMessage: String, errorCode: Int) :
        Resource<T>(null, errorMessage, errorCode)

    companion object {
        fun <T> noNetworkError(context: Context?): Resource.Error<T> {
            return Resource.Error(
                context?.getString(R.string.check_internet_connection)
                    ?: "No network!", 0
            )
        }
    }

    override fun toString(): String {
        var size = 0
        if (data is Collection<*>) {
            size = data.size
        }
        return "Resource(data.size=$size, errorMessage=$errorMessage, errorCode=$errorCode, class=${javaClass})"
    }

}