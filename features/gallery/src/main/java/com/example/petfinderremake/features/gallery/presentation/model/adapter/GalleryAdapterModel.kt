package com.example.petfinderremake.features.gallery.presentation.model.adapter

sealed class GalleryAdapterModel(
    val adapterId: Int,
    val adapterViewType: GalleryAdapterEnum
) {
    data class Image(
        val id: Int,
        val url: String,
        val viewType: GalleryAdapterEnum = GalleryAdapterEnum.IMAGE
    ) : GalleryAdapterModel(id, viewType)

    data class Video(
        val id: Int,
        val url: String,
        val viewType: GalleryAdapterEnum = GalleryAdapterEnum.VIDEO
    ) : GalleryAdapterModel(id, viewType)
}

enum class GalleryAdapterEnum {
    IMAGE,
    VIDEO
}

fun List<String>.createGalleryAdapterModel(galleryAdapterEnum: GalleryAdapterEnum) : List<GalleryAdapterModel> {
   return when (galleryAdapterEnum) {
        GalleryAdapterEnum.IMAGE -> {
            mapIndexed { index, item ->
                GalleryAdapterModel.Image(
                    index,
                    item
                )
            }
        }

        GalleryAdapterEnum.VIDEO -> {
            mapIndexed { index, item ->
                GalleryAdapterModel.Video(
                    index,
                    item
                )
            }
        }
    }
}