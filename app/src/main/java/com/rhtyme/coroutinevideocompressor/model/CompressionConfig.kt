package com.rhtyme.coroutinevideocompressor.model

import com.rhtyme.coroutinevideocompressor.utils.CompressorUtils
import java.lang.StringBuilder

data class CompressionConfig(
    var path: String,
    var name: String,
    var duration: Long, // in milliseconds
    var resolution: VideoResolution,
    var quality: VideoQuality,
    var format: VideoFormat,
    var outputFilePath: String = ""
) {


    fun toFFMPEGCommand(outputFolder: String): String {
        this.outputFilePath = "$outputFolder/" + CompressorUtils.removeFileExtension(name) + "." + format.ext
        val sb = StringBuilder()
        sb.append("-i ").append(path)
            .append(" -crf ").append(quality.crf)
//            .append(" -preset fast ")
            .append(" -speed 8 ")
            .append(" -s ").append(resolution.toString())
            .append(" ").append(this.outputFilePath)
        return sb.toString()
    }


    class Builder {

        private lateinit var path: String
        private lateinit var name: String
        private var duration: Long = 0
        private lateinit var resolution: VideoResolution
        private lateinit var quality: VideoQuality
        private lateinit var format: VideoFormat


        fun resolution(resolution: VideoResolution): Builder {
            this.resolution = resolution
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun quality(quality: VideoQuality): Builder {
            this.quality = quality
            return this
        }

        fun duration(duration: Long ): Builder {
            this.duration = duration
            return this
        }

        fun quality(quality: String): Builder {

            this.quality = when {
                quality.equals("high", ignoreCase = true) -> {
                    VideoQuality.HIGH
                }
                quality.equals("medium", ignoreCase = true) -> {
                    VideoQuality.MEDUIM
                }
                quality.equals("low", ignoreCase = true) -> {
                    VideoQuality.LOW
                }
                else -> {
                    VideoQuality.HIGH
                }
            }
            return this
        }

        fun format(format: VideoFormat): Builder {
            this.format = format
            return this
        }

        fun format(format: String): Builder {
            this.format = when {
                format.startsWith("mp4", ignoreCase = true) -> {
                    VideoFormat.MP4
                }
                format.startsWith("3gp", ignoreCase = true) -> {
                    VideoFormat._3GP
                }
                format.startsWith("mp3", ignoreCase = true) -> {
                    VideoFormat.MP3
                }
                else -> {
                    VideoFormat.MP4
                }
            }
            return this
        }

        fun build(): CompressionConfig {
            return CompressionConfig(path, name, duration, resolution, quality, format)
        }

        companion object {

            fun with(path: String): Builder {
                val builder = Builder()
                builder.path = path
                return builder
            }
        }
    }
}