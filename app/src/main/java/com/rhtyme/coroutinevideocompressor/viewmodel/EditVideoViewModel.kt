package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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


    fun fetchMediaInformation(path: String) = viewModelScope.launch(coroutineContextProvider.ui()) {
        mediaInformationLiveData.postValue(Resource.Loading())
        try {
            Timber.d("")
            val info = withContext(coroutineContextProvider.io()) {
                editVideoRepo.fetchMediaInformation(path)
            }
            mediaInformationLiveData.postValue(Resource.Success(info))

        } catch (e: Exception) {
            mediaInformationLiveData.postValue(Resource.Error(e.message ?: "", 0))
        }
    }

    fun startCompression(context: Context, config: CompressionConfig) = viewModelScope.launch(coroutineContextProvider.ui()) {
        if (compressInformationLiveData.value is Resource.Loading) {
            return@launch
        }

        compressInformationLiveData.value = Resource.Loading()

        try {
            val result = withContext(coroutineContextProvider.io()) {
                editVideoRepo.startCompression(context, config, publishProgress)
            }
            compressInformationLiveData.postValue(Resource.Success(result.result))
        } catch (e: Exception) {
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