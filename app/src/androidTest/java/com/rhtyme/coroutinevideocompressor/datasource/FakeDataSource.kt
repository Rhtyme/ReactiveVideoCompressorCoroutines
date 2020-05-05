package com.rhtyme.coroutinevideocompressor.datasource

import com.arthenica.mobileffmpeg.MediaInformation
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.CompressionConfig
import com.rhtyme.coroutinevideocompressor.model.ProgressiveResult
import io.reactivex.Observable

object FakeDataSource {


    fun albumFiles(): List<AlbumFile> {
        val albums = arrayListOf<AlbumFile>()

        (1..10).forEach {
            albums.add(AlbumFile())
        }
        return albums
    }

    fun mediaInfo(): MediaInformation {
        return MediaInformation()
    }

    fun albumFile(): AlbumFile {
        return AlbumFile()
    }

    fun compressionConfig(): CompressionConfig {
        return CompressionConfig.Builder().build()
    }

    fun progressResult(): ProgressiveResult.Progress<AlbumFile> {
        val progress = 0.1
        return ProgressiveResult.Progress(progress)
    }

    fun albumResult(albumFile: AlbumFile): ProgressiveResult.Result<AlbumFile> {
        return ProgressiveResult.Result(albumFile)
    }

    fun progressiveResultAlbumFile(
        progress: ProgressiveResult.Progress<AlbumFile>,
        albumFile: ProgressiveResult.Result<AlbumFile>
    ): Observable<ProgressiveResult<AlbumFile>> {
        return Observable.create { emitter ->
            emitter.onNext(progress)
            emitter.onNext(albumFile)
            emitter.onComplete()
        }
    }
}