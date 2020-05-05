package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.coroutinevideocompressor.data.repo.EditVideoRepo
import com.rhtyme.coroutinevideocompressor.datasource.FakeDataSource
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.ProgressiveResult
import com.rhtyme.coroutinevideocompressor.model.Resource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class EditVideoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var editVideoViewModel: EditVideoViewModel

    private lateinit var context: Context

    @Mock
    private lateinit var observerMediaInformationLiveData: Observer<Resource<MediaInformation>>

    @Mock
    private lateinit var observerCompressInformationLiveData: Observer<Resource<AlbumFile>>

    @Mock
    private lateinit var editVideoRepo: EditVideoRepo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().targetContext
        editVideoViewModel = EditVideoViewModel(context, editVideoRepo)
        editVideoViewModel.coroutineContextProvider.setupTestMode()

        editVideoViewModel.mediaInformationLiveData.observeForever(observerMediaInformationLiveData)
        editVideoViewModel.compressInformationLiveData.observeForever(
            observerCompressInformationLiveData
        )

    }


    @Test
    fun testFetchMediaInformation() {

        val mediaInfo = FakeDataSource.mediaInfo()
        val resp = Resource.Success(mediaInfo)

        `when`(editVideoRepo.fetchMediaInformation("")).thenAnswer { mediaInfo }
        editVideoViewModel.fetchMediaInformation("")

        verify(observerMediaInformationLiveData).onChanged(resp)
    }

    @Test
    fun testStartCompression() {
        val albumFile = FakeDataSource.albumFile()
        val albumFileResult = FakeDataSource.albumResult(albumFile)
        val progressResult = FakeDataSource.progressResult()
        val compressionConfig = FakeDataSource.compressionConfig()

        val albumFileResp = Resource.Success(albumFile)

        val publishProgress: (progress: ProgressiveResult.Progress<AlbumFile>) -> Unit = {}

        val fakeObservable =
            FakeDataSource.progressiveResultAlbumFile(progressResult, albumFileResult)
        `when`(editVideoRepo.startCompression(context, compressionConfig, publishProgress))
            .thenAnswer { fakeObservable }

        editVideoViewModel.startCompression(context, compressionConfig)

        verify(observerCompressInformationLiveData).onChanged(albumFileResp)
    }

    @After
    fun cleanUp() {

    }

}