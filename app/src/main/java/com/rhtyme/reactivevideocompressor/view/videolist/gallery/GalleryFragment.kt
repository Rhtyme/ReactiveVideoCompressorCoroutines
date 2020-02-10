package com.rhtyme.reactivevideocompressor.view.videolist.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.rhtyme.reactivevideocompressor.R
import com.rhtyme.reactivevideocompressor.model.Resource
import com.rhtyme.reactivevideocompressor.view.base.BaseFragment
import com.rhtyme.reactivevideocompressor.view.editvideo.EditVideoDialogActivity
import com.rhtyme.reactivevideocompressor.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class GalleryFragment: BaseFragment() {

    val galleryViewModel: GalleryViewModel by inject()

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

        galleryViewModel.albumFilesLiveData.observe(this, Observer {
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

        galleryViewModel.loadGallery()
    }


    private fun initAdapter(context: Context?) {

        val mLayoutManager = GridLayoutManager(context, 4)

        albumGalleryRecycler?.layoutManager = mLayoutManager

        albumAdapter = AlbumAdapter(context, false)

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
                    startActivity(EditVideoDialogActivity.getInstanceIntent(album, it))
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

        const val TITLE = "Gallery"

        fun getInstance(bundle: Bundle?): GalleryFragment {
            val fragment =  GalleryFragment()
            bundle?.let {
                fragment.arguments = it
            }
            return fragment
        }
    }
}