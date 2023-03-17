package com.codemave.mobilecomputing.ui.reminders

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.Notification
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun Reminder(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    selectedCategory: Category
) {
    val viewModel: ReminderViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        ReminderList(
            list = viewState.notifications,
            context,
            navController,
            selectedCategory
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Notification>,
    context: Context,
    navController: NavController,
    selectedCategory: Category
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                notification = item,
                modifier = Modifier.fillParentMaxWidth(),
                context,
                navController,
                selectedCategory
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    notification: Notification,
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    selectedCategory: Category,
    viewModel: ReminderViewModel = viewModel()
) {
    if (notification.reminderTime <= Date().time && selectedCategory == Category(
            1,
            "Reminders"
        ) || !notification.notificationSeen && selectedCategory == Category(2, "Show all")
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val coroutineScope = rememberCoroutineScope()
            val (divider, notificationTitle, notificationTime, notificationDate, editIcon, deleteIcon) = createRefs()

            Divider(
                Modifier.constrainAs(divider) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = notification.notificationTitle,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationTitle) {
                    linkTo(
                        start = parent.start,
                        end = editIcon.start,
                        startMargin = 24.dp,
                        endMargin = 120.dp,
                        bias = 0f
                    )
                    top.linkTo(parent.top, margin = 10.dp)
                    width = Dimension.preferredWrapContent
                }
            )

            Text(
                text = notification.notificationTime,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationTime) {
                    linkTo(
                        start = parent.start,
                        end = notificationDate.start,
                        startMargin = 8.dp,
                        endMargin = 8.dp,
                        bias = 0f
                    )
                    top.linkTo(notificationTitle.bottom, margin = 5.dp)
                    width = Dimension.preferredWrapContent
                }
            )

            var date = notification.notificationDate
            if (notification.recurringEnabled) {
                date = "$date     Daily"
            }

            Text(
                text = date,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(notificationDate) {
                    linkTo(
                        start = notificationTime.end,
                        end = editIcon.start,
                        startMargin = 8.dp,
                        endMargin = 80.dp,
                        bias = 0f
                    )
                    top.linkTo(notificationTitle.bottom, margin = 5.dp)
                    width = Dimension.preferredWrapContent
                }
            )

            // Edit icon
            IconButton(
                onClick = {
                    println("ID: ${notification.notificationId}")
                    navController.navigate(
                        "edit/{id}"
                            .replace(
                                oldValue = "{id}",
                                newValue = notification.notificationId.toString()
                            )
                    )
                },
                modifier = Modifier
                    .size(50.dp)
                    .padding(6.dp)
                    .constrainAs(editIcon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        end.linkTo(deleteIcon.start)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit reminder"
                )
            }

            // Delete icon
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteReminder(notification)
                    }
                    Toast.makeText(context, "Notification deleted", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(50.dp)
                    .padding(6.dp)
                    .constrainAs(deleteIcon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}