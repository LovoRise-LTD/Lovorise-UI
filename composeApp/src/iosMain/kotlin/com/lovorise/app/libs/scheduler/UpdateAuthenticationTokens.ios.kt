package com.lovorise.app.libs.scheduler

import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import platform.BackgroundTasks.*
import platform.Foundation.*

actual fun scheduleTokenRefreshTask(context: PlatformContext?){
//    scheduleTokenRefreshTask1(context)
}


@OptIn(ExperimentalForeignApi::class)
fun scheduleTokenRefreshTask1(context: PlatformContext?) {

    // Register the background task first, so the system knows how to handle it
    BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
        TASK_IDENTIFIER,
        null
    ) { task ->
        if (task is BGProcessingTask) {
            CoroutineScope(Dispatchers.IO).launch {
                handleBackgroundTask(task)
            }
        }
    }

    // Cancel any previously scheduled task (not explicitly needed as tasks are overwritten)
    cancelTokenRefreshTask()

    // Schedule a background task
    val request = BGProcessingTaskRequest(TASK_IDENTIFIER)
    request.requiresNetworkConnectivity = true
    request.earliestBeginDate = NSDate().dateByAddingTimeInterval(REFRESH_TIME_IN_HOUR * 3600.0)

    try {
        BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
    } catch (e: Exception) {
        NSLog("Failed to schedule task:")
        e.printStackTrace()
    }
}

fun cancelTokenRefreshTask() {
    // Cancel any previously scheduled task with the same identifier
    BGTaskScheduler.sharedScheduler.cancelAllTaskRequests()
}

private suspend fun handleBackgroundTask(task: BGProcessingTask) {
    // Perform the token refresh logic here
    task.setTaskCompletedWithSuccess(refreshToken(null))
    scheduleTokenRefreshTask(null)
}

