package com.example.petfinderremake.common.presentation.screen.animalDetails

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.ext.withLifecycleOwner
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

interface AnimalDetailsReceiver {

    fun Fragment.setupAnimalDetailsReceiver(
        animalDetailsSender: () -> AnimalDetailsSender,
        navigateToAnimalDetails: (id: Long) -> Unit
    ): Job {
        val sender = animalDetailsSender()
        return withLifecycleOwner {
            sender.getAnimalDetailsEvent().collectLatest { event ->
                when (event) {
                    is AnimalDetailsSender.SenderEvent.NavigateToAnimalDetails -> {
                        navigateToAnimalDetails(event.id)
                    }
                }
            }
        }
    }
}