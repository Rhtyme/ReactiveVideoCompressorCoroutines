package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rhtyme.coroutinevideocompressor.data.repo.GalleryRepo
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.Resource
import com.rhtyme.coroutinevideocompressor.view.videolist.gallery.GalleryFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.lang.ref.WeakReference

class GalleryViewModel(context: Context, val galleryRepo: GalleryRepo) :
    BaseViewModel(WeakReference(context)) {

    val albumFilesLiveData = MutableLiveData<Resource<List<AlbumFile>>>()

    fun loadGallery() = launch {
        Timber.tag(GalleryFragment.SEQUENCE_TAG).d("*** on loadGallery(): ${System.nanoTime()}")

        val context = weakContext.get() ?: return@launch
        if (albumFilesLiveData.value is Resource.Loading) {
            return@launch
        }
        albumFilesLiveData.postValue(Resource.Loading())

        try {
            Timber.tag(GalleryFragment.SEQUENCE_TAG).d("### before withContext getAllMedia(): ${System.nanoTime()}")
            val files = withContext(Dispatchers.IO) {
                Timber.tag(GalleryFragment.SEQUENCE_TAG).d("### inside withContext getAllMedia(): ${System.nanoTime()}")
                galleryRepo.getAllMedia(context)
            }
            Timber.tag(GalleryFragment.SEQUENCE_TAG).d("### after withContext getAllMedia(): ${System.nanoTime()}")
            Timber.tag(GALLERY_TAG).d("onSuccess: ${files.size}")
            albumFilesLiveData.postValue(Resource.Success(files))
        } catch (e: Exception) {
            Timber.tag(GALLERY_TAG).d("onError: $e")
            albumFilesLiveData.postValue(Resource.Error("not loaded", 0))
        }
    }

    companion object {
        const val GALLERY_TAG = "GALLERY_TAG"
    }
}