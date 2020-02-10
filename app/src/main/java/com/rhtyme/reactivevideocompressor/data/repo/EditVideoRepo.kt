package com.rhtyme.reactivevideocompressor.data.repo

import android.content.Context
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.reactivevideocompressor.model.AlbumFile
import com.rhtyme.reactivevideocompressor.model.CompressionConfig
import com.rhtyme.reactivevideocompressor.model.ProgressiveResult
import com.rhtyme.reactivevideocompressor.model.toStringT
import com.rhtyme.reactivevideocompressor.utils.provider.ReactiveVCompressorProvider
import com.rhtyme.reactivevideocompressor.viewmodel.EditVideoViewModel
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import java.io.File
import java.lang.Exception

class EditVideoRepo(val galleryRepo: GalleryRepo) {

    fun fetchMediaInformation(path: String): Single<MediaInformation> {
        return Single.create {
            var mediaInfo: MediaInformation? = null
            try {
                mediaInfo = FFmpeg.getMediaInformation(path)
                if (mediaInfo != null) {
                    it.onSuccess(mediaInfo)
                } else {
                    it.onError(Throwable("mediaInfo is null"))
                }
            } catch (e: Exception) {
                it.onError(Throwable(e))
            }
        }
    }

    fun startCompression(
        context: Context,
        config: CompressionConfig
    ): Observable<ProgressiveResult<AlbumFile>> {
        return Observable.create { emitter ->
            val outputFolder = context.externalCacheDir?.path
            if (outputFolder.isNullOrEmpty()) {

                emitter.onError(Exception("can not write to SD card"))
                return@create
            }
            val cmd = config.toFFMPEGCommand(outputFolder)
            Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                .d("startCompression, cmd: $cmd")
            val outFile = File(config.outputFilePath)

            if (outFile.exists()){
                outFile.delete()
            }

            Config.resetStatistics()
            Config.enableStatisticsCallback { statistics ->
                Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                    .d("startCompression, statistics:\n ${statistics.toStringT()}")
                val progress = statistics.time.toDouble() / config.duration.toDouble()
                emitter.onNext(ProgressiveResult.Progress(progress))

            }
            val result = FFmpeg.execute(cmd)

            if (result == 0 && outFile.exists()) {

                val outputUri = ReactiveVCompressorProvider.getUriForFile(
                    context,
                    ReactiveVCompressorProvider.getProviderName(context),
                    outFile
                )

                val albumFile = galleryRepo.getMediaMetaData(context, outputUri, outFile)
                Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                    .d("startCompression, result:\n $albumFile")
                emitter.onNext(ProgressiveResult.Result(albumFile))
                emitter.onComplete()
            } else {
                Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                    .d("startCompression, result:\n $result, output: ${FFmpeg.getLastCommandOutput()}")
                emitter.onError(Exception("result: $result"))
            }

            Config.resetStatistics()
        }
    }

    companion object {
        const val EDIT_VIDEO_REPO_T = "EDIT_VIDEO_REPO_T"
    }
}