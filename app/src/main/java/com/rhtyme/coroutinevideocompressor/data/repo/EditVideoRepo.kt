package com.rhtyme.coroutinevideocompressor.data.repo

import android.content.Context
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.CompressionConfig
import com.rhtyme.coroutinevideocompressor.model.ProgressiveResult
import com.rhtyme.coroutinevideocompressor.model.toStringT
import com.rhtyme.coroutinevideocompressor.utils.provider.CoroutineVCompressorProvider
import com.rhtyme.coroutinevideocompressor.viewmodel.EditVideoViewModel
import timber.log.Timber
import java.io.File
import java.lang.Exception

class EditVideoRepo(val galleryRepo: GalleryRepo) {

    @Throws(Exception::class)
    fun  fetchMediaInformation(path: String): MediaInformation {
        var mediaInfo: MediaInformation? = null
        mediaInfo = FFmpeg.getMediaInformation(path)
        if (mediaInfo != null) {
            return mediaInfo
        } else {
            throw Exception("mediaInfo is null")
        }
    }

    @Throws(Exception::class)
    fun startCompression(
        context: Context,
        config: CompressionConfig,
        progressListener: (ProgressiveResult.Progress<AlbumFile>) -> Unit
    ): ProgressiveResult.Result<AlbumFile> {
        val outputFolder = context.externalCacheDir?.path
        if (outputFolder.isNullOrEmpty()) {
            throw Exception("can not write to SD card")
        }
        val cmd = config.toFFMPEGCommand(outputFolder)
        Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
            .d("startCompression, cmd: $cmd")
        val outFile = File(config.outputFilePath)

        if (outFile.exists()) {
            outFile.delete()
        }

        Config.resetStatistics()
        Config.enableStatisticsCallback { statistics ->
            Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                .d("startCompression, statistics:\n ${statistics.toStringT()}")
            val progress = statistics.time.toDouble() / config.duration.toDouble()
            progressListener.invoke(ProgressiveResult.Progress(progress))
        }
        val result = FFmpeg.execute(cmd)

        Config.resetStatistics()

        if (result == 0 && outFile.exists()) {
            val outputUri = CoroutineVCompressorProvider.getUriForFile(
                context,
                CoroutineVCompressorProvider.getProviderName(context),
                outFile
            )

            val albumFile = galleryRepo.getMediaMetaData(context, outputUri, outFile)
            Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                .d("startCompression, result:\n $albumFile")
            return ProgressiveResult.Result(albumFile)
        } else {
            Timber.tag(EditVideoViewModel.EDIT_VIDEO_VM_T)
                .d("startCompression, result:\n $result, output: ${FFmpeg.getLastCommandOutput()}")

            throw Exception("result: $result")
        }
    }

    companion object {
        const val EDIT_VIDEO_REPO_T = "EDIT_VIDEO_REPO_T"
    }
}