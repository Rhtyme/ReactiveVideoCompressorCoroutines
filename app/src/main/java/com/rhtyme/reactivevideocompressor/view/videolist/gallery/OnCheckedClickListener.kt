package com.rhtyme.reactivevideocompressor.view.videolist.gallery

import android.widget.CompoundButton

interface OnCheckedClickListener {

    /**
     * Compound button is clicked.
     *
     * @param button   view.
     * @param position the position in the list.
     */
    fun onCheckedClick(button: CompoundButton, position: Int)

}