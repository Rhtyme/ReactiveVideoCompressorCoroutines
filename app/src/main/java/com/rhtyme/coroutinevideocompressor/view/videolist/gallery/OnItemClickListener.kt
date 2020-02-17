package com.rhtyme.coroutinevideocompressor.view.videolist.gallery

import android.view.View

interface OnItemClickListener {

    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    fun onItemClick(view: View, position: Int)
    fun onMoreCilck(view: View, position: Int)

}