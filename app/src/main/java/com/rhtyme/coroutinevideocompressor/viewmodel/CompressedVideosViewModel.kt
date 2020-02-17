package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rhtyme.coroutinevideocompressor.data.repo.GalleryRepo
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.lang.ref.WeakReference

class CompressedVideosViewModel(context: Context, val galleryRepo: GalleryRepo) :
    BaseViewModel(WeakReference(context)) {

    val albumFilesLiveData = MutableLiveData<Resource<List<AlbumFile>>>()

    fun loadCompressedVideos() = launch {
        val context = weakContext.get() ?: return@launch
        if (albumFilesLiveData.value is Resource.Loading) {
            return@launch
        }
        albumFilesLiveData.postValue(Resource.Loading())
        try {
            val albumFiles = withContext(Dispatchers.IO) {
                galleryRepo.scanCacheFolderForCompressedVideos(context)
            }
            Timber.tag(COMPRESSED_TAG).d("loadCompressedVideos onSuccess: ${albumFiles.size}")
            albumFilesLiveData.postValue(Resource.Success(albumFiles))

        } catch (e: Exception) {
            Timber.tag(COMPRESSED_TAG).d("loadCompressedVideos onError: $e")
            albumFilesLiveData.postValue(Resource.Error("not loaded", 0))
        }
    }

    companion object {
        const val COMPRESSED_TAG = "COMPRESSED_TAG"
    }

}