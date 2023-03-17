package com.codemave.mobilecomputing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    indices = [
        Index("id", unique = true)
    ]
)

data class Notification(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val notificationId: Long = 0,
    @ColumnInfo(name = "title") val notificationTitle: String,
    @ColumnInfo(name = "locationX") val locationX: Double?,
    @ColumnInfo(name = "locationY") val locationY: Double?,
    @ColumnInfo(name = "time") val notificationTime: String,
    @ColumnInfo(name = "date") val notificationDate: String,
    @ColumnInfo(name = "reminderTime") val reminderTime: Long,
    @ColumnInfo(name = "creationTime") val creationTime: Long,
    @ColumnInfo(name = "creatorId") val creatorId: String,
    @ColumnInfo(name = "seen") val notificationSeen: Boolean,
    @ColumnInfo(name = "notificationEnabled") val notificationEnabled: Boolean,
    @ColumnInfo(name = "recurringEnabled") val recurringEnabled: Boolean
)
