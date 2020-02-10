package com.rhtyme.reactivevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rhtyme.reactivevideocompressor.data.repo.GalleryRepo
import com.rhtyme.reactivevideocompressor.model.AlbumFile
import com.rhtyme.reactivevideocompressor.model.Resource
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
import java.lang.ref.WeakReference

class GalleryViewModel(context: Context, val galleryRepo: GalleryRepo) :
    BaseViewModel(WeakReference(context)) {

    val albumFilesLiveData = MutableLiveData<Resource<List<AlbumFile>>>()

    fun loadGallery() {
        val context = weakContext.get() ?: return
        if (albumFilesLiveData.value is Resource.Loading) {
            return
        }
        albumFilesLiveData.postValue(Resource.Loading())

        val disposable = galleryRepo.getAllMedia(context)
            .compose(rxSingleSchedulers.applySchedulers())
            .subscribeWith(object : DisposableSingleObserver<List<AlbumFile>>() {
                override fun onSuccess(t: List<AlbumFile>) {
                    Timber.tag(GALLERY_TAG).d("onSuccess: ${t.size}")
                    albumFilesLiveData.postValue(Resource.Success(t))
                }

                override fun onError(e: Throwable) {
                    Timber.tag(GALLERY_TAG).d("onError: $e")
                    albumFilesLiveData.postValue(Resource.Error("not loaded", 0))
                }
            })
        addDisposable(disposable)
    }

    companion object {
        const val GALLERY_TAG = "GALLERY_TAG"
    }
}