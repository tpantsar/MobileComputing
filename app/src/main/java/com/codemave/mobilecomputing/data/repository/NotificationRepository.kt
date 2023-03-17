package com.codemave.mobilecomputing.data.repository

import com.codemave.mobilecomputing.data.entity.Notification
import com.codemave.mobilecomputing.data.room.NotificationDao
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val notificationDao: NotificationDao
) {
    fun getNotificationWithId(id: Long): Notification = notificationDao.getNotificationWithId(id)

    fun getNotifications(): Flow<List<Notification>> {
        return notificationDao.getNotifications()
    }

    /**
     * Add a notification
     * @return the id of the notification
     */
    suspend fun addNotification(notification: Notification): Long {
        return notificationDao.insert(notification)
    }

    suspend fun deleteNotification(notification: Notification): Int {
        return notificationDao.delete(notification)
    }

    suspend fun updateNotification(notification: Notification) {
        return notificationDao.update(notification)
    }

    fun updateSeenState(notification: Notification) {
        return notificationDao.update2(notification)
    }
}