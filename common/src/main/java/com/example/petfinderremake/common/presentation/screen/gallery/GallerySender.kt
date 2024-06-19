package com.example.petfinderremake.common.presentation.screen.gallery

import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.domain.model.animal.media.checkIfMediaExists
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

interface GallerySender {
    val gallerySenderSubject: PublishSubject<SenderEvent>
    fun getGalleryEvent(): Observable<SenderEvent> = gallerySenderSubject
        .hide()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun navigateToGallery(galleryArg: GalleryArg)

    sealed interface SenderEvent {
        data object DisplayNoInfo : SenderEvent
        data class NavigateToGallery(val media: Media) : SenderEvent
    }

    sealed interface GalleryArg {
        data class GalleryId(val id: Long) : GalleryArg
        data object NoArg : GalleryArg

        companion object {
            fun GalleryArg.runAction(
                actionWithId: (Long) -> Unit = { throw Exception() },
                actionNoArg: () -> Unit = { throw Exception() }
            ) {
                when (this) {
                    is GalleryId -> actionWithId(id)
                    is NoArg -> actionNoArg()
                }
            }
        }
    }

    fun runGalleryAction(media: Media) {
        val mediaExists = media.checkIfMediaExists()

        if (mediaExists) {
            gallerySenderSubject.onNext(SenderEvent.NavigateToGallery(media))
        } else {
            gallerySenderSubject.onNext(SenderEvent.DisplayNoInfo)
        }

    }
}

