package com.rhtyme.coroutinevideocompressor.data.repo


import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import com.rhtyme.coroutinevideocompressor.model.AlbumFolder
import com.rhtyme.coroutinevideocompressor.utils.provider.CoroutineVCompressorProvider
import timber.log.Timber
import java.io.File
import java.util.*


class GalleryRepo {

    /**
     * Image attribute.
     */
    private val IMAGES = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.LATITUDE,
        MediaStore.Images.Media.LONGITUDE,
        MediaStore.Images.Media.SIZE
    )

    /**
     * Video attribute.
     */
    private val VIDEOS = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.LATITUDE,
        MediaStore.Video.Media.LONGITUDE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DURATION
    )

    private fun scanAllVideoFiles(
        mContext: Context,
        albumFolderMap: MutableMap<String, AlbumFolder>,
        allFileFolder: AlbumFolder
    ) {
        scanUriForVideoFiles(
            mContext, allFileFolder,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, albumFolderMap
        )
    }

    private fun scanUriForVideoFiles(
        mContext: Context,
        allFileFolder: AlbumFolder,
        uri: Uri,
        albumFolderMap: MutableMap<String, AlbumFolder>? = null
    ) {
        val contentResolver = mContext.contentResolver
        val cursor = contentResolver.query(
            uri, VIDEOS, null, null, null
        )

        if (cursor != null && cursor.count >= 1) {
            while (cursor.moveToNext()) {

//                val indexedname = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                val filename = cursor.getString(indexedname)

                val path = cursor.getString(0)
                val bucketName = cursor.getString(1)
                val mimeType = cursor.getString(2)
                val addDate = cursor.getLong(3)
                val latitude = cursor.getFloat(4)
                val longitude = cursor.getFloat(5)
                val size = cursor.getLong(6)
                val duration = cursor.getLong(7)

                if (path.isNullOrEmpty() || size <= 0) {
                    continue
                }

                val videoFile = AlbumFile()
                videoFile.mediaType = AlbumFile.TYPE_VIDEO
                videoFile.path = path
                videoFile.bucketName = bucketName
                videoFile.mimeType = mimeType
                videoFile.addDate = addDate
                videoFile.latitude = latitude
                videoFile.longitude = longitude
                videoFile.size = size
                videoFile.duration = duration

                videoFile.fileName = bucketName

                allFileFolder.albumFiles.add(videoFile)

                if (albumFolderMap != null) {
                    var albumFolder = albumFolderMap[bucketName]

                    if (albumFolder != null)
                        albumFolder.albumFiles.add(videoFile)
                    else {
                        albumFolder = AlbumFolder()
                        albumFolder.name = bucketName
                        albumFolder.albumFiles.add(videoFile)

                        albumFolderMap[bucketName] = albumFolder
                    }
                }
            }
            cursor.close()
        }

    }

    /**
     * Get all the multimedia files, including videos and pictures.
     */
    @Throws(Resources.NotFoundException::class)
    fun getAllMedia(context: Context): List<AlbumFile> {
        val albumFolderMap = HashMap<String, AlbumFolder>()
        val allFileFolder = AlbumFolder()
        allFileFolder.isChecked = true
        allFileFolder.name =
            (context.getString(com.rhtyme.coroutinevideocompressor.R.string.album_all_images_videos))

//            scanImageFile(context, albumFolderMap, allFileFolder)
        scanAllVideoFiles(context, albumFolderMap, allFileFolder)

        val albumFolders = ArrayList<AlbumFolder>()
        allFileFolder.albumFiles.sort()
        albumFolders.add(allFileFolder)

        for ((_, albumFolder) in albumFolderMap) {
            albumFolder.albumFiles.sort()
            albumFolders.add(albumFolder)
        }
        val list = albumFolders.getOrNull(0)?.albumFiles ?: emptyList<AlbumFile>()
        if (list.isNotEmpty()) {
            return list
        } else {
            throw Resources.NotFoundException("can not load gallery")
        }
    }

    @Throws(Exception::class)
    fun scanCacheFolderForCompressedVideos(context: Context): List<AlbumFile> {
        val outputFolder = context.externalCacheDir
        if (outputFolder == null || !outputFolder.exists()) {
            throw Exception("can not write to SD card")
        }
        Timber.tag(GALLERY_TAG).d("outputFolderPath: ${outputFolder.path} ")
        val allFileFolder = AlbumFolder()

        for (file in outputFolder.listFiles() ?: emptyArray()) {
            val outputUri = CoroutineVCompressorProvider.getUriForFile(
                context,
                CoroutineVCompressorProvider.getProviderName(context),
                file
            )
            Timber.tag(GALLERY_TAG)
                .d("outputFolder: ${file.path}, outputUri: $outputUri ")

            val album = getMediaMetaData(context, outputUri, file)
            Timber.tag(GALLERY_TAG)
                .d("got album: ${album}")

            allFileFolder.albumFiles.add(album)
        }

        if (allFileFolder.albumFiles.isNullOrEmpty()) {
             throw Exception("the files are empty or not found")
        } else {
            return allFileFolder.albumFiles
        }
    }

    fun getMediaMetaData(
        mContext: Context,
        uri: Uri,
        file: File
    ): AlbumFile {

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(mContext, uri)
        val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
//        val addDate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE).toLong()
        val mimeType = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

        val videoFile = AlbumFile()
        videoFile.mediaType = AlbumFile.TYPE_VIDEO
        videoFile.mimeType = mimeType
//        videoFile.addDate = addDate
        videoFile.duration = duration

        videoFile.size = file.length()
        videoFile.path = file.absolutePath
        videoFile.fileName = file.name

        retriever.release()
        return videoFile
    }


    companion object {
        const val GALLERY_TAG = "GALLERY_TAG"
    }

}