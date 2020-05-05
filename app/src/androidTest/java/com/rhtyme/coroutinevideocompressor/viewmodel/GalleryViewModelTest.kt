package com.rhtyme.coroutinevideocompressor.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rhtyme.coroutinevideocompressor.data.repo.GalleryRepo
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.Resource
import com.rhtyme.coroutinevideocompressor.datasource.FakeDataSource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class GalleryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var galleryViewModel: GalleryViewModel

    private lateinit var context: Context

    @Mock
    private lateinit var observerAlbumFiles: Observer<Resource<List<AlbumFile>>>


    @Mock
    private lateinit var galleryRepo: GalleryRepo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
//        galleryRepo = mock(GalleryRepo::class.java)
        context = InstrumentationRegistry.getInstrumentation().targetContext
        galleryViewModel = GalleryViewModel(context,galleryRepo)
        galleryViewModel.coroutineContextProvider.setupTestMode()
        galleryViewModel.albumFilesLiveData.observeForever(observerAlbumFiles)

    }


    @Test
    fun testLoadGallery() {
        val albums = FakeDataSource.albumFiles()
        val resp = Resource.Success(albums)
        `when`(galleryRepo.getAllMedia(context)).thenAnswer { albums }
        galleryViewModel.loadGallery()

        verify(observerAlbumFiles).onChanged(resp)
    }

    @After
    fun cleanUp() {

    }


}