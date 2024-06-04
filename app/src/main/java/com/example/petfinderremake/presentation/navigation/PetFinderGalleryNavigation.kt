package com.example.petfinderremake.presentation.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petfinderremake.R
import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.common.presentation.navigation.model.GALLERY_KEY
import com.example.petfinderremake.common.presentation.navigation.model.GalleryNavArg
import com.example.petfinderremake.common.presentation.navigation.model.toGalleryNavArg
import com.example.petfinderremake.common.presentation.navigation.model.toString
import com.squareup.moshi.Moshi
import javax.inject.Inject

class PetFinderGalleryNavigation @Inject constructor(
    private val moshi: Moshi
) : GalleryNavigation {

    override fun navigateToGallery(fragment: Fragment, media: Media) =
        with(fragment) {

            val (images, videos) = with(media) {
                photos.map { it.getLargestAvailablePhoto() } to videos.map { it.video }
            }
            val galleryNavArg = GalleryNavArg(images, videos)
            val argument = galleryNavArg.toString(moshi)
            val bundle = bundleOf(GALLERY_KEY to argument)
            findNavController().navigate(R.id.galleryFragment, bundle)
        }

    override fun getGalleryNavArg(fragment: Fragment): GalleryNavArg = with(fragment) {
        val argument = arguments?.getString(GALLERY_KEY) ?: throw Exception()
        return argument.toGalleryNavArg(moshi)
    }

}