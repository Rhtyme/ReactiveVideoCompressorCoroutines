package com.rhtyme.coroutinevideocompressor.model

data class VideoResolution(
    val width: Long,
    val height: Long
) {

    override fun toString(): String {
        return "${width}x${height}"
    }
}