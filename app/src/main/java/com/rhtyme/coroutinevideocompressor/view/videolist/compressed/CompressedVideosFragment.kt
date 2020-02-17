package com.rhtyme.coroutinevideocompressor.view.videolist.compressed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rhtyme.coroutinevideocompressor.R
import com.rhtyme.coroutinevideocompressor.model.Resource
import com.rhtyme.coroutinevideocompressor.view.base.BaseFragment
import com.rhtyme.coroutinevideocompressor.view.editvideo.EditVideoDialogActivity
import com.rhtyme.coroutinevideocompressor.view.videolist.gallery.AlbumAdapter
import com.rhtyme.coroutinevideocompressor.view.videolist.gallery.OnCheckedClickListener
import com.rhtyme.coroutinevideocompressor.view.videolist.gallery.OnItemClickListener
import com.rhtyme.coroutinevideocompressor.viewmodel.CompressedVideosViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class CompressedVideosFragment: BaseFragment() {

    val compressedVideosViewModel: CompressedVideosViewModel by inject()

    lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(context)

        compressedVideosViewModel.albumFilesLiveData.observe(this, Observer {
            when(it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        albumAdapter.setAlbumFiles(it)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                }
            }
        })

        compressedVideosViewModel.loadCompressedVideos()
    }


    private fun initAdapter(context: Context?) {

        val mLayoutManager = GridLayoutManager(context, 4)

        albumGalleryRecycler?.layoutManager = mLayoutManager

        albumAdapter = AlbumAdapter(context, false, true)

        albumAdapter.setAddClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
//                getPresenter().clickCamera(view)

            }

            override fun onMoreCilck(view: View, position: Int) {
            }
        })
        albumAdapter.setCheckedClickListener(object : OnCheckedClickListener {
            override fun onCheckedClick(button: CompoundButton, position: Int) {
//                Timber.tag(ATTACH_TAG).d("onCheckedClick: $position")
                albumAdapter.mAlbumFiles.getOrNull(position)?.let {}
            }
        })
        albumAdapter.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
//                getPresenter().tryPreviewItem(position)

                val album = albumAdapter.mAlbumFiles.getOrNull(position)?: return
                context?.let {
                    startActivity(EditVideoDialogActivity.getInstanceIntent(album, it, false))
                }
            }

            override fun onMoreCilck(view: View, position: Int) {

            }
        })

        albumGalleryRecycler?.adapter = albumAdapter
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            pbLoading.visibility = View.VISIBLE
        } else {
            pbLoading.visibility = View.GONE
        }
    }
    companion object {

        const val TITLE = "Compressed videos"

        fun getInstance(bundle: Bundle?): CompressedVideosFragment {
            val fragment =  CompressedVideosFragment()
            bundle?.let {
                fragment.arguments = it
            }
            return fragment
        }
    }

}