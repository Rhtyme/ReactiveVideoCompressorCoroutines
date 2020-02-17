package com.rhtyme.coroutinevideocompressor.view.videolist.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rhtyme.coroutinevideocompressor.R
import com.rhtyme.coroutinevideocompressor.model.AlbumFile
import timber.log.Timber

class AlbumAdapter(
    context: Context?,
    private val hasCamera: Boolean,
    private val needMoreOptions: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mInflater: LayoutInflater

    var mAlbumFiles: MutableList<AlbumFile> = arrayListOf()

    private var mAddPhotoClickListener: OnItemClickListener? = null
    private var mItemClickListener: OnItemClickListener? = null
    private var mCheckedClickListener: OnCheckedClickListener? = null

    init {
        this.mInflater = LayoutInflater.from(context)
    }

    fun setAlbumFiles(albumFiles: List<AlbumFile>) {
        Timber.tag("gallery_tag").d("album: $${albumFiles[0]}")
        mAlbumFiles.clear()
        mAlbumFiles.addAll(albumFiles)
        notifyDataSetChanged()
    }

    fun setAddClickListener(addPhotoClickListener: OnItemClickListener) {
        this.mAddPhotoClickListener = addPhotoClickListener
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setCheckedClickListener(checkedClickListener: OnCheckedClickListener) {
        this.mCheckedClickListener = checkedClickListener
    }

    override fun getItemCount(): Int {
        val camera = if (hasCamera) 1 else 0
        return mAlbumFiles.size + camera
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> {
                return if (hasCamera) TYPE_BUTTON else TYPE_VIDEO
            }
            else -> {
                return TYPE_VIDEO
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_BUTTON -> {
                return ButtonViewHolder(
                    mInflater.inflate(
                        R.layout.attachment_album_item_content_button,
                        parent,
                        false
                    ), mAddPhotoClickListener
                )
            }
            TYPE_VIDEO -> {
                val videoViewHolder = VideoHolder(
                    mInflater.inflate(R.layout.attachment_album_item_content_video, parent, false),
                    hasCamera, needMoreOptions,
                    mItemClickListener,
                    mCheckedClickListener
                )
//                videoViewHolder.mCheckBox.visibility = View.VISIBLE
                return videoViewHolder
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var position = position
        when (getItemViewType(position)) {
            TYPE_BUTTON -> {
            }// Nothing.
            TYPE_VIDEO -> {
                val mediaHolder = holder as MediaViewHolder
                val camera = if (hasCamera) 1 else 0
                position = holder.getAdapterPosition() - camera
                val albumFile = mAlbumFiles[position]
                mediaHolder.setData(albumFile)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    private class ButtonViewHolder internal constructor(
        itemView: View,
        private val mItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (mItemClickListener != null && v === itemView) {
                mItemClickListener.onItemClick(v, 0)
            }
        }
    }

    private class ImageHolder internal constructor(
        itemView: View, private val hasCamera: Boolean,
        private val mItemClickListener: OnItemClickListener?,
        private val mCheckedClickListener: OnCheckedClickListener?
    ) : MediaViewHolder(itemView), View.OnClickListener {

        val mIvImage: ImageView
        val mCheckBox: AppCompatCheckBox

        private val mLayoutLayer: FrameLayout

        init {

            mIvImage = itemView.findViewById(R.id.iv_album_content_image)
            mCheckBox = itemView.findViewById(R.id.check_box)
            mLayoutLayer = itemView.findViewById(R.id.layout_layer)

            itemView.setOnClickListener(this)
            mCheckBox.setOnClickListener(this)
            mLayoutLayer.setOnClickListener(this)
        }

        override fun setData(albumFile: AlbumFile) {
            mCheckBox.isChecked = albumFile.isChecked

            Glide.with(mIvImage.getContext())
                .load(albumFile.path)
                .into(mIvImage)

            mLayoutLayer.visibility = if (albumFile.isDisable) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View) {
            if (v === itemView) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener?.onItemClick(v, adapterPosition - camera)
            } else if (v === mCheckBox) {
                val camera = if (hasCamera) 1 else 0
                mCheckedClickListener?.onCheckedClick(mCheckBox, adapterPosition - camera)
            } else if (v === mLayoutLayer) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener?.onItemClick(v, adapterPosition - camera)
            }
        }
    }

    private class VideoHolder internal constructor(
        itemView: View, private val hasCamera: Boolean,
        needMoreOptions: Boolean,
        private val mItemClickListener: OnItemClickListener?,
        private val mCheckedClickListener: OnCheckedClickListener?
    ) : MediaViewHolder(itemView), View.OnClickListener {

        val mIvImage: ImageView
        val mCheckBox: AppCompatCheckBox
        val mTvDuration: TextView

        val mMoreOptions: View

        private val mLayoutLayer: FrameLayout

        init {

            mIvImage = itemView.findViewById(R.id.iv_album_content_image)
            mCheckBox = itemView.findViewById(R.id.check_box)
            mTvDuration = itemView.findViewById(R.id.tv_duration)
            mLayoutLayer = itemView.findViewById(R.id.layout_layer)
            mMoreOptions = itemView.findViewById(R.id.viewMoreOptions)

            if (needMoreOptions) {
                mMoreOptions.visibility = View.VISIBLE
            }

            itemView.setOnClickListener(this)
            mCheckBox.setOnClickListener(this)
            mLayoutLayer.setOnClickListener(this)
            mMoreOptions.setOnClickListener(this)
        }

        override fun setData(albumFile: AlbumFile) {
            mCheckBox.isChecked = albumFile.isChecked
            //            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));

            Glide.with(mIvImage.getContext())
                .load(albumFile.path)
                .into(mIvImage)

            mLayoutLayer.visibility = if (albumFile.isDisable) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View) {
            if (v === itemView) {
                val camera = if (hasCamera) 1 else 0
                mItemClickListener?.onItemClick(v, adapterPosition - camera)
            } else if (v === mCheckBox) {
                val camera = if (hasCamera) 1 else 0
                mCheckedClickListener?.onCheckedClick(mCheckBox, adapterPosition - camera)
            } else if (v === mLayoutLayer) {
                if (mItemClickListener != null) {
                    val camera = if (hasCamera) 1 else 0
                    mItemClickListener.onItemClick(v, adapterPosition - camera)
                }
            } else if (v === mMoreOptions) {
                if (mItemClickListener != null) {
                    val camera = if (hasCamera) 1 else 0
                    mItemClickListener.onMoreCilck(v, adapterPosition - camera)
                }
            }
        }
    }

    private abstract class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Bind Item data.
         */
        abstract fun setData(albumFile: AlbumFile)
    }

    companion object {

        private val TYPE_BUTTON = 1
        private val TYPE_VIDEO = 2

    }
}