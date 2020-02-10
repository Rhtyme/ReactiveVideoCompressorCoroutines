package com.rhtyme.reactivevideocompressor.di

import android.content.Context
import com.rhtyme.reactivevideocompressor.data.Constants
import com.rhtyme.reactivevideocompressor.data.PreferenceHelper
import com.rhtyme.reactivevideocompressor.data.repo.EditVideoRepo
import com.rhtyme.reactivevideocompressor.data.repo.GalleryRepo
import com.rhtyme.reactivevideocompressor.data.repo.MainRepo
import com.rhtyme.reactivevideocompressor.viewmodel.CompressedVideosViewModel
import com.rhtyme.reactivevideocompressor.viewmodel.EditVideoViewModel
import com.rhtyme.reactivevideocompressor.viewmodel.GalleryViewModel
import com.rhtyme.reactivevideocompressor.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.experimental.dsl.viewModel


val appModule = module {
    single { PreferenceHelper(androidContext().getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE)) }

}

val repoModule = module {

    single { MainRepo(get()) }
    single { GalleryRepo() }
    single { EditVideoRepo(get()) }
}


val viewModelModule = module {
    viewModel<MainViewModel>()
    viewModel<GalleryViewModel>()
    viewModel<EditVideoViewModel>()
    viewModel<CompressedVideosViewModel>()
}
