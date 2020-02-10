package com.rhtyme.reactivevideocompressor.utils

import com.rhtyme.reactivevideocompressor.model.VideoResolution

object CompressorUtils {

    fun fetchVideoResolutionArray(maxWidth: Long, maxHeight: Long): ArrayList<VideoResolution> {
        val vArray = arrayListOf<VideoResolution>()
        vArray.add(
            VideoResolution(
                maxWidth,
                maxHeight
            )
        )
        vArray.add(
            VideoResolution(
                (maxWidth * 0.75).toLong(),
                (maxHeight * 0.75).toLong()
            )
        )
        vArray.add(
            VideoResolution(
                (maxWidth * 0.5).toLong(),
                (maxHeight * 0.5).toLong()
            )
        )
        return vArray
    }

    fun humanReadableByteCountBin(bytes: Long) = when {
        bytes == Long.MIN_VALUE || bytes < 0 -> "N/A"
        bytes < 1024L -> "$bytes B"
        bytes <= 0xfffccccccccccccL shr 40 -> "%.1f KB".format(bytes.toDouble() / (0x1 shl 10))
        bytes <= 0xfffccccccccccccL shr 30 -> "%.1f MB".format(bytes.toDouble() / (0x1 shl 20))
        bytes <= 0xfffccccccccccccL shr 20 -> "%.1f GB".format(bytes.toDouble() / (0x1 shl 30))
        bytes <= 0xfffccccccccccccL shr 10 -> "%.1f TB".format(bytes.toDouble() / (0x1 shl 40))
        bytes <= 0xfffccccccccccccL -> "%.1f PiB".format((bytes shr 10).toDouble() / (0x1 shl 40))
        else -> "%.1f EiB".format((bytes shr 20).toDouble() / (0x1 shl 40))
    }

    fun fetchSize(bitrate: Long, duration: Long): String {
        return humanReadableByteCountBin((bitrate / 8) * (duration / 1000))
    }


    fun removeFileExtension(fileName: String): String {
        return if (fileName.indexOf(".") > 0) {
            fileName.substring(0, fileName.lastIndexOf("."))
        } else {
            fileName
        }
    }

}