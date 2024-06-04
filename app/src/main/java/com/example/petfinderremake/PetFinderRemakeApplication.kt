package com.example.petfinderremake

import android.app.Application
import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.logging.Logger
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PetFinderRemakeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //todo multibinding?
        Logger.init()
    }


}