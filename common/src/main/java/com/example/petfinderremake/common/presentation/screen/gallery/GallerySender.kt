package com.example.petfinderremake.common.presentation.screen.gallery

import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.domain.model.animal.media.checkIfMediaExists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface GallerySender {
    val gallerySenderEvent: Channel<SenderEvent>
    fun getGalleryEvent(): Flow<SenderEvent> = gallerySenderEvent.receiveAsFlow()
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

    fun CoroutineScope.runGalleryAction(media: Media): Job {
        val mediaExists = media.checkIfMediaExists()
        return launch {
            if (mediaExists) {
                gallerySenderEvent.send(SenderEvent.NavigateToGallery(media))
            } else {
                gallerySenderEvent.send(SenderEvent.DisplayNoInfo)
            }
        }
    }
}

