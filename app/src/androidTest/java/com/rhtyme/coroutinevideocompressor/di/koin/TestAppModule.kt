package com.rhtyme.coroutinevideocompressor.di.koin

import android.content.Context
import com.rhtyme.coroutinevideocompressor.data.Constants
import com.rhtyme.coroutinevideocompressor.data.PreferenceHelper
import com.rhtyme.coroutinevideocompressor.data.repo.EditVideoRepo
import com.rhtyme.coroutinevideocompressor.data.repo.GalleryRepo
import com.rhtyme.coroutinevideocompressor.data.repo.MainRepo
import com.rhtyme.coroutinevideocompressor.viewmodel.*
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
