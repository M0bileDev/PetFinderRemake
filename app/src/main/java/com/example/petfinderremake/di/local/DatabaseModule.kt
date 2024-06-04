package com.example.petfinderremake.di.local

import android.content.Context
import androidx.room.Room
import com.example.petfinderremake.common.data.local.dao.NotificationDao
import com.example.petfinderremake.common.data.local.db.PETFINDER_DATABASE
import com.example.petfinderremake.common.data.local.db.PetFinderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object DatabaseModule {

    @[Provides Singleton]
    fun providePetFinderDatabase(
        @ApplicationContext context: Context
    ): PetFinderDatabase {
        return Room
            .databaseBuilder(
                context,
                PetFinderDatabase::class.java,
                PETFINDER_DATABASE
            )
            .build()
    }

    @[Provides Singleton]
    fun provideNotificationDao(
        petFinderDatabase: PetFinderDatabase
    ): NotificationDao {
        return petFinderDatabase.notificationDao()
    }

}