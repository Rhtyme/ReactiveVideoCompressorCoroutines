package com.rhtyme.reactivevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.reactivevideocompressor.data.repo.EditVideoRepo
import com.rhtyme.reactivevideocompressor.model.*
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
import java.lang.ref.WeakReference

class EditVideoViewModel(context: Context, val editVideoRepo: EditVideoRepo) :
    BaseViewModel(WeakReference(context)) {

    val mediaInformationLiveData = MutableLiveData<Resource<MediaInformation>>()

    val compressInformationLiveData = MutableLiveData<Resource<AlbumFile>>()


    fun fetchMediaInformation(path: String) {

        mediaInformationLiveData.postValue(Resource.Loading())
        val disposable = editVideoRepo.fetchMediaInformation(path)
            .compose(rxSingleSchedulers.applySchedulers())
            .subscribeWith(object : DisposableSingleObserver<MediaInformation>() {
                override fun onSuccess(t: MediaInformation) {
                    Timber.tag(EDIT_VIDEO_VM_T)
                        .d("fetchMediaInformation, onSuccess: ${t.toStringT()}")
                    mediaInformationLiveData.postValue(Resource.Success(t))

                }

                override fun onError(e: Throwable) {
                    Timber.tag(EDIT_VIDEO_VM_T).d("fetchMediaInformation, onError: $e")
                    mediaInformationLiveData.postValue(Resource.Error(e.message ?: "", 0))
                }

            })

        addDisposable(disposable)
    }

    fun startCompression(context: Context, config: CompressionConfig) {
        if (compressInformationLiveData.value is Resource.Loading) {
            return
        }
        var outResult: AlbumFile? = null

        compressInformationLiveData.value = Resource.Loading()
        val disposable = editVideoRepo.startCompression(context, config)
            .compose(rxObservableSchedulers.applySchedulers())
            .subscribeWith(object : DisposableObserver<ProgressiveResult<AlbumFile>>() {
                override fun onComplete() {
                    Timber.tag(EDIT_VIDEO_VM_T).d("startCompression, onComplete")
                    if (outResult != null) {
                        compressInformationLiveData.postValue(Resource.Success(outResult))
                    } else {
                        compressInformationLiveData.postValue(Resource.Error("empty", 0))
                    }
                }

                override fun onNext(t: ProgressiveResult<AlbumFile>) {
                    Timber.tag(EDIT_VIDEO_VM_T).d("startCompression, onNext: $t")
                    if (t is ProgressiveResult.Progress) {
                        compressInformationLiveData.postValue(Resource.Loading(t.progress))
                    } else if (t is ProgressiveResult.Result) {
                        outResult = t.result
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.tag(EDIT_VIDEO_VM_T).d("startCompression, onError: $e")
                    compressInformationLiveData.postValue(Resource.Error("onError: $e", 1))
                }

            })
        addDisposable(disposable)
    }

    companion object {
        const val EDIT_VIDEO_VM_T = "EDIT_VIDEO_VM_T"
    }
}