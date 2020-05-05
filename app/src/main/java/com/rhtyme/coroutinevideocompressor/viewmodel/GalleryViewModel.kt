package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

class GalleryViewModel(context: Context,
                       val galleryRepo: GalleryRepo) :
    BaseViewModel(WeakReference(context)) {

    val albumFilesLiveData = MutableLiveData<Resource<List<AlbumFile>>>()

    fun loadGallery() = viewModelScope.launch(coroutineContextProvider.ui()) {
        val context = weakContext.get() ?: return@launch
        if (albumFilesLiveData.value is Resource.Loading) {
            return@launch
        }
        albumFilesLiveData.postValue(Resource.Loading())

        try {
            val files = withContext(coroutineContextProvider.io()) {
                galleryRepo.getAllMedia(context)
            }
            albumFilesLiveData.postValue(Resource.Success(files))
        } catch (e: Exception) {
            albumFilesLiveData.postValue(Resource.Error("not loaded", 0))
        }
    }

    companion object {
        const val GALLERY_TAG = "GALLERY_TAG"
    }
}