package com.example.petfinderremake.common.presentation.screen.gallery

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.utils.commonString
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

interface GalleryReceiver {

    fun Fragment.setupGalleryReceiver(
        gallerySender: () -> GallerySender,
        navigateToGallery: (media: Media) -> Unit
    ): Job {
        val sender = gallerySender()
        return withLifecycleOwner {
            sender.getGalleryEvent().collectLatest { event ->
                when (event) {
                    is GallerySender.SenderEvent.DisplayNoInfo -> {
                        Snackbar.make(
                            requireView(),
                            getString(commonString.no_media_to_display),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    is GallerySender.SenderEvent.NavigateToGallery -> {
                        navigateToGallery(event.media)
                    }
                }
            }
        }
    }

}