package com.codemave.mobilecomputing.ui.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Notification
import com.codemave.mobilecomputing.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit

class ReminderViewModel(
    private val notificationRepository: NotificationRepository = Graph.notificationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationViewModelState())

    val state: StateFlow<NotificationViewModelState>
        get() = _state

    suspend fun saveReminder(notification: Notification): Long {

        // Put recurring reminder back to default state
        recurringReminderEnabled = false

        // If a notification is recurring, eg. daily
        return if (notification.recurringEnabled) {
            val result = notificationRepository.addNotification(notification)
            println("ID: $result")
            setRecurringNotification(notification, result)
            result
        } else {
            val result = notificationRepository.addNotification(notification)
            println("ID: $result")
            setOneTimeNotification(notification, result)
            result
        }
    }

    suspend fun deleteReminder(notification: Notification): Int {
        return notificationRepository.deleteNotification(notification)
    }

    suspend fun updateReminder(notification: Notification) {
        val result = notificationRepository.updateNotification(notification)
        println("ID: ${notification.notificationId}")
        setOneTimeNotification(notification, notification.notificationId)
        return result
    }

    fun getNotificationWithId(id: Long): Notification {
        return notificationRepository.getNotificationWithId(id)
    }

    init {
        createNotificationChannel(context = Graph.appContext)
        viewModelScope.launch {
            notificationRepository.getNotifications().collect { notifications ->
                _state.value = NotificationViewModelState(notifications)
            }
        }
    }
}

data class NotificationViewModelState(
    val notifications: List<Notification> = emptyList()
)

private fun setOneTimeNotification(reminder: Notification, id: Long) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val delay: Long = (reminder.reminderTime - reminder.creationTime) / 1000

    println(reminder.reminderTime)
    println(reminder.creationTime)

    val notificationWorker = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay - 20, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotification(reminder, id)
                updateSeen(reminder)
            }
        }
}

// Sets a periodic daily notification for a single reminder
private fun setRecurringNotification(reminder: Notification, id: Long) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val delay: Long = (reminder.reminderTime - reminder.creationTime) / 1000

    println(reminder.reminderTime)
    println(reminder.creationTime)

    // Repeat notification every day
    val interval = Duration.ofDays(1)

    val notificationWorker = PeriodicWorkRequestBuilder<ReminderWorker>(interval)
        .setInitialDelay(delay - 20, TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotification(reminder, id)
                updateSeen(reminder)
            }
        }
}

private fun createReminderNotification(reminder: Notification, id: Long) {

    val notificationId = id.toInt()
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(reminder.notificationTitle)
        .setContentText(reminder.notificationTime + " " + reminder.notificationDate)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}

private fun updateSeen(
    reminder: Notification,
    notificationRepository: NotificationRepository = Graph.notificationRepository
) {
    notificationRepository.updateSeenState(
        Notification(
            notificationId = reminder.notificationId,
            notificationTitle = reminder.notificationTitle,
            locationX = reminder.locationX,
            locationY = reminder.locationY,
            notificationTime = reminder.notificationTime,
            notificationDate = reminder.notificationDate,
            reminderTime = reminder.reminderTime,
            creationTime = reminder.creationTime,
            creatorId = reminder.creatorId,
            notificationSeen = true,
            notificationEnabled = true,
            recurringEnabled = recurringReminderEnabled
        )
    )
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescription"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}