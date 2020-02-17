package com.rhtyme.coroutinevideocompressor.utils

object TimeUtils {

    fun secToTime(sec: Int): String? {
        val seconds = sec % 60
        var minutes = sec / 60
        if (minutes >= 60) {
            val hours = minutes / 60
            minutes %= 60
            if (hours >= 24) {
                val days = hours / 24
                return String.format(
                    "%d days %02d:%02d:%02d",
                    days,
                    hours % 24,
                    minutes,
                    seconds
                )
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        return String.format("00:%02d:%02d", minutes, seconds)
    }

}