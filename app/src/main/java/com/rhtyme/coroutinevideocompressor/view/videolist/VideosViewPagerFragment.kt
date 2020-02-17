package com.rhtyme.coroutinevideocompressor.view.videolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.rhtyme.coroutinevideocompressor.R
import com.rhtyme.coroutinevideocompressor.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_videos_view_pager.*

class VideosViewPagerFragment : BaseFragment() {

    private lateinit var videosViewPagerAdapter: VideosViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_videos_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        videosViewPagerAdapter = VideosViewPagerAdapter(childFragmentManager)
        viewPager.adapter = videosViewPagerAdapter
        mTab.setupWithViewPager(viewPager)

        mToolbar.setTitle(R.string.app_name)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }


    companion object {

        fun getInstance(): VideosViewPagerFragment {
            val fragment = VideosViewPagerFragment()
            return fragment
        }
    }
}