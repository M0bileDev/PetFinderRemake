package com.example.petfinderremake.common.presentation.screen.animalDetails

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface AnimalDetailsSender {
    val animalDetailsSenderEvent: Channel<SenderEvent>
    fun getAnimalDetailsEvent(): Flow<SenderEvent> = animalDetailsSenderEvent.receiveAsFlow()
    fun navigateToAnimalDetails(id: Long)

    sealed interface SenderEvent {
        data class NavigateToAnimalDetails(val id: Long) : SenderEvent
    }

    fun CoroutineScope.runAnimalDetailsAction(id: Long): Job {
        return launch {
            animalDetailsSenderEvent.send(SenderEvent.NavigateToAnimalDetails(id))
        }
    }
}