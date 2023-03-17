package com.codemave.mobilecomputing

import android.content.Context
import androidx.room.Room
import com.codemave.mobilecomputing.data.repository.NotificationRepository
import com.codemave.mobilecomputing.data.room.Database

object Graph {
    lateinit var database: Database
        private set

    lateinit var appContext: Context

    val notificationRepository by lazy {
        NotificationRepository(
            notificationDao = database.notificationDao()
        )
    }

    fun provide(context: Context) {
        appContext = context
        database = Room.databaseBuilder(context, Database::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}