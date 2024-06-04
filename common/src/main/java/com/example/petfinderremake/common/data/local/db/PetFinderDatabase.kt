package com.example.petfinderremake.common.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petfinderremake.common.data.local.dao.NotificationDao
import com.example.petfinderremake.common.data.local.model.NotificationEntity

const val PETFINDER_DATABASE = "petfinder_database"

@Database(entities = [NotificationEntity::class], version = 1)
abstract class PetFinderDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}