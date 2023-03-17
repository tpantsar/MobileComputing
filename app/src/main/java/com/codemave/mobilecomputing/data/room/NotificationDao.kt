package com.codemave.mobilecomputing.data.room

import androidx.room.*
import com.codemave.mobilecomputing.data.entity.Notification
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotificationDao {

    @Query(value = "SELECT * FROM notifications WHERE title = :title")
    abstract fun getNotificationWithTitle(title: String): Notification?

    @Query("SELECT * FROM notifications WHERE id = :id")
    abstract fun getNotificationWithId(id: Long): Notification

    @Query("SELECT * FROM notifications")
    abstract fun getNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Notification): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Notification)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update2(entity: Notification)

    @Delete
    abstract suspend fun delete(entity: Notification): Int
}