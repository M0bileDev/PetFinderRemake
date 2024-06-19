package com.example.petfinderremake.common.presentation.screen.gallery

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.utils.UserActionInterval
import com.example.petfinderremake.common.presentation.utils.commonString
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job

interface GalleryReceiver {

    fun Fragment.setupGalleryReceiver(
        gallerySender: () -> GallerySender,
        navigateToGallery: (media: Media) -> Unit,
        jobBlock: (Job) -> Unit,
        onDispose: (Disposable) -> Unit
    ) = withLifecycleOwner(
        disposableBlock = {
            gallerySender()
                .getGalleryEvent()
                .throttleFirst(
                    UserActionInterval.default.elapsedTime,
                    UserActionInterval.default.timeUnit
                )
                .subscribe { event ->
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
        },
        jobBlock = jobBlock,
        onDispose = onDispose
    )
}