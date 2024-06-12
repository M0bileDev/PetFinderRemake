package com.example.petfinderremake.common.presentation.screen.animalDetails

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.utils.UserActionInterval.Companion.default
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job

interface AnimalDetailsReceiver {

    fun Fragment.setupAnimalDetailsReceiver(
        animalDetailsSender: () -> AnimalDetailsSender,
        navigateToAnimalDetails: (id: Long) -> Unit,
        jobBlock: (Job) -> Unit,
        onDispose: (Disposable) -> Unit
    ) =
        withLifecycleOwner(
            disposableBlock = {
                animalDetailsSender()
                    .getAnimalDetailsEvent()
                    .throttleFirst(default.elapsedTime, default.timeUnit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { event ->
                        when (event) {
                            is AnimalDetailsSender.SenderEvent.NavigateToAnimalDetails -> {
                                navigateToAnimalDetails(event.id)
                            }
                        }
                    }
            },
            jobBlock = jobBlock,
            onDispose = onDispose
        )

}