package com.example.petfinderremake.common.presentation.navigation.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.io.IOException

@JsonClass(generateAdapter = true)
data class GalleryNavArg(
    val images: List<String> = emptyList(),
    val videos: List<String> = emptyList()
) {
    class GalleryNavArgConvertError :
        IllegalStateException("An exception occurs during conversion GalleryNavArg")
}

fun String.toGalleryNavArg(moshi: Moshi): GalleryNavArg {
    return try {
        val adapter = GalleryNavArgJsonAdapter(moshi)
        adapter.fromJson(this)!!
    } catch (ioe: IOException) {
        throw GalleryNavArg.GalleryNavArgConvertError()
    }
}

fun GalleryNavArg.toString(moshi: Moshi): String {
    val adapter = GalleryNavArgJsonAdapter(moshi)
    return adapter.toJson(this)
        ?: throw GalleryNavArg.GalleryNavArgConvertError()
}
