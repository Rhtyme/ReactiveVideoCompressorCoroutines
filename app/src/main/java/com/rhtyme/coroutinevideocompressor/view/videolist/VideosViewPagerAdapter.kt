package com.rhtyme.coroutinevideocompressor.view.videolist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rhtyme.coroutinevideocompressor.view.videolist.compressed.CompressedVideosFragment
import com.rhtyme.coroutinevideocompressor.view.videolist.gallery.GalleryFragment

class VideosViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = 2

    override fun getItem(i: Int): Fragment {
        val fragment: Fragment = when (i) {
            0 -> {
                GalleryFragment.getInstance(null)
            }
            1 -> {
                CompressedVideosFragment.getInstance(null)
            }
            else -> {
                throw NotImplementedError("support only two type of fragments")
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> {
                GalleryFragment.TITLE
            }
            1 -> {
                CompressedVideosFragment.TITLE
            }
            else -> {
                ""
            }
        }
    }
}