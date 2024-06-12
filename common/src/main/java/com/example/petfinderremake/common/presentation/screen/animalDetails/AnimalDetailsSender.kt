package com.example.petfinderremake.common.presentation.screen.animalDetails

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

interface AnimalDetailsSender {
    val animalDetailsSenderSubject: PublishSubject<SenderEvent>
    fun getAnimalDetailsEvent(): Observable<SenderEvent> = animalDetailsSenderSubject.hide()
    fun navigateToAnimalDetails(id: Long)

    sealed interface SenderEvent {
        data class NavigateToAnimalDetails(val id: Long) : SenderEvent
    }

    fun runAnimalDetailsAction(id: Long) {
        animalDetailsSenderSubject.onNext(SenderEvent.NavigateToAnimalDetails(id))
    }
}