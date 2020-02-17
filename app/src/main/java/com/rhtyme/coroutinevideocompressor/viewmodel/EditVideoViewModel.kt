package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.coroutinevideocompressor.data.repo.EditVideoRepo
import com.rhtyme.coroutinevideocompressor.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.lang.ref.WeakReference

class EditVideoViewModel(context: Context, val editVideoRepo: EditVideoRepo) :
    BaseViewModel(WeakReference(context)) {

    val mediaInformationLiveData = MutableLiveData<Resource<MediaInformation>>()

    val compressInformationLiveData = MutableLiveData<Resource<AlbumFile>>()


    fun fetchMediaInformation(path: String) = launch {
        mediaInformationLiveData.postValue(Resource.Loading())
        try {
            Timber.d("")
            val info = withContext(Dispatchers.IO) {
                editVideoRepo.fetchMediaInformation(path)
            }
            Timber.tag(EDIT_VIDEO_VM_T)
                .d("fetchMediaInformation, onSuccess: ${info.toStringT()}")
            mediaInformationLiveData.postValue(Resource.Success(info))

        } catch (e: Exception) {
            Timber.tag(EDIT_VIDEO_VM_T).d("fetchMediaInformation, onError: $e")
            mediaInformationLiveData.postValue(Resource.Error(e.message ?: "", 0))
        }
    }

    fun startCompression(context: Context, config: CompressionConfig) = launch {
        if (compressInformationLiveData.value is Resource.Loading) {
            return@launch
        }

        compressInformationLiveData.value = Resource.Loading()

        try {
            val result = withContext(Dispatchers.IO) {
                editVideoRepo.startCompression(context, config, publishProgress)
            }
            Timber.tag(EDIT_VIDEO_VM_T).d("startCompression, onComplete: $result")
            compressInformationLiveData.postValue(Resource.Success(result.result))
        } catch (e: Exception) {
            Timber.tag(EDIT_VIDEO_VM_T).d("startCompression, onError: $e")
            compressInformationLiveData.postValue(Resource.Error("onError: $e", 1))
        }
    }

    val publishProgress: (progress: ProgressiveResult.Progress<AlbumFile>) -> Unit = {
        //IO thread (background)
        compressInformationLiveData.postValue(Resource.Loading(it.progress))
    }

    companion object {
        const val EDIT_VIDEO_VM_T = "EDIT_VIDEO_VM_T"
    }
}