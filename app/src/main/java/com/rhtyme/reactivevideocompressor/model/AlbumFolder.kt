package com.rhtyme.reactivevideocompressor.model

import java.util.ArrayList

class AlbumFolder {

    /**
     * Folder name.
     */
    var name: String? = null
    /**
     * Image list in folder.
     */
    var albumFiles: ArrayList<AlbumFile> = ArrayList()
    /**
     * checked.
     */
    var isChecked: Boolean = false
}