package com.codemave.mobilecomputing.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemave.mobilecomputing.data.entity.Notification

@Database(
    entities = [Notification::class],
    version = 8,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}