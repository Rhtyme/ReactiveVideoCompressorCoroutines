package com.rhtyme.reactivevideocompressor.model

import androidx.annotation.IntDef
import com.rhtyme.reactivevideocompressor.utils.CompressorUtils
import java.io.Serializable

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class  AlbumFile : Comparable<AlbumFile>, Serializable {

    /**
     * File path.
     */
    var path: String? = null
    /**
     * Folder mName.
     */
    var bucketName: String? = null
    /**
     * File mime type.
     */
    var mimeType: String? = null
    /**
     * Add date.
     */
    var addDate: Long = 0
    /**
     * Latitude
     */
    var latitude: Float = 0.toFloat()
    /**
     * Longitude.
     */
    var longitude: Float = 0.toFloat()
    /**
     * Size.
     */
    var size: Long = 0
    /**
     * Duration.
     */
    var duration: Long = 0
    /**
     * Thumb path.
     */
    var thumbPath: String? = null
    /**
     * MediaType.
     */
    @get:MediaType
    var mediaType: Int = 0
    /**
     * Checked.
     */
    var isChecked: Boolean = false
    /**
     * Enabled.
     */
    var isDisable: Boolean = false

    /**
     * Filename
     */
    var fileName: String = ""

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(TYPE_IMAGE, TYPE_VIDEO)
    annotation class MediaType


    override fun compareTo(o: AlbumFile): Int {
        val time = o.addDate - addDate
        if (time > Integer.MAX_VALUE)
            return Integer.MAX_VALUE
        else if (time < -Integer.MAX_VALUE)
            return -Integer.MAX_VALUE
        return time.toInt()
    }

    override fun equals(obj: Any?): Boolean {
        if (obj != null && obj is AlbumFile) {
            val o = obj as AlbumFile?
            val inPath = o!!.path
            if (path != null && inPath != null) {
                return path == inPath
            }
        }
        return super.equals(obj)
    }

    override fun hashCode(): Int {
        return if (path != null) path!!.hashCode() else super.hashCode()
    }

    override fun toString(): String {
        return "AlbumFile(path=$path, mimeType=$mimeType, size=$size, size.human: ${CompressorUtils.humanReadableByteCountBin(size)}, duration=$duration, thumbPath=$thumbPath, isChecked=$isChecked, isDisable=$isDisable)"
    }


    companion object {
        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
    }

}