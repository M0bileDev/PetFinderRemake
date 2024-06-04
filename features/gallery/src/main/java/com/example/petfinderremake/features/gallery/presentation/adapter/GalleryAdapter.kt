package com.example.petfinderremake.features.gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderremake.common.databinding.GalleryItemImageBinding
import com.example.petfinderremake.common.databinding.GalleryItemVideoBinding
import com.example.petfinderremake.common.ext.ScaleType
import com.example.petfinderremake.common.ext.setImage

class GalleryAdapter :
    ListAdapter<com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel, RecyclerView.ViewHolder>(
        DiffCallback()
    ) {

    private lateinit var galleryItemImageBinding: GalleryItemImageBinding
    private lateinit var galleryItemVideoBinding: GalleryItemVideoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.IMAGE.ordinal -> {
                galleryItemImageBinding =
                    GalleryItemImageBinding.inflate(layoutInflater, parent, false)
                GalleryItemImageViewHolder(galleryItemImageBinding)
            }

            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.VIDEO.ordinal -> {
                galleryItemVideoBinding =
                    GalleryItemVideoBinding.inflate(layoutInflater, parent, false)
                GalleryItemVideoViewHolder(galleryItemVideoBinding)
            }

            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (holder.itemViewType) {
            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.IMAGE.ordinal -> {
                (holder as GalleryItemImageViewHolder).bind(
                    item as com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel.Image
                )
            }

            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.VIDEO.ordinal -> {
                (holder as GalleryItemVideoViewHolder).bind(
                    (item as com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel.Video)
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return when (item.adapterViewType) {
            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.IMAGE -> com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.IMAGE.ordinal
            com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.VIDEO -> com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum.VIDEO.ordinal
        }
    }

    inner class GalleryItemImageViewHolder(private val binding: GalleryItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel.Image) {
            binding.galleryItemImageView.setImage(image.url, ScaleType.FIT_CENTER)
        }
    }

    inner class GalleryItemVideoViewHolder(private val binding: GalleryItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel.Video) {
            // TODO: Not implemented yet
        }
    }

    private class DiffCallback :
        DiffUtil.ItemCallback<com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel,
            newItem: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel
        ): Boolean {
            return oldItem.adapterId == newItem.adapterId
        }

        override fun areContentsTheSame(
            oldItem: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel,
            newItem: com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel
        ): Boolean {
            return oldItem == newItem
        }

    }

}