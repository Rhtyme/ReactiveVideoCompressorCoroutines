package com.rhtyme.reactivevideocompressor.model

import com.arthenica.mobileffmpeg.MediaInformation
import com.arthenica.mobileffmpeg.Statistics
import com.arthenica.mobileffmpeg.StreamInformation
import com.google.gson.Gson

class MediaInfoT : MediaInformation() {
}


fun StreamInformation.toStringT(): String {
    return "index: $index, type: $type, "
}

fun MediaInformation.toStringT(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun Statistics.toStringT(): String {
    val gson = Gson()
    return gson.toJson(this)
}

enum class VideoQuality(var crf: Int) {
    HIGH(17), MEDUIM(23), LOW(28)
}

enum class VideoFormat(var ext: String) {
    MP4("mp4"), _3GP("3gp"), MP3("mp3")
}