package com.lovorise.app.libs.scheduler


import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import coil3.PlatformContext
import java.util.concurrent.TimeUnit


actual fun scheduleTokenRefreshTask(context:PlatformContext?) {
    if (context == null) return
    val workManager = WorkManager.getInstance(context)

    // Cancel any existing work with the same name
    workManager.cancelUniqueWork(REFRESH_TASK_NAME)

    // Schedule the new work
    val workRequest = PeriodicWorkRequestBuilder<TokenRefreshWorker>(REFRESH_TIME_IN_HOUR.toLong(), TimeUnit.HOURS)
        .setInitialDelay(4, TimeUnit.HOURS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    workManager.enqueueUniquePeriodicWork(
        REFRESH_TASK_NAME, // Unique name for the task
        ExistingPeriodicWorkPolicy.UPDATE, // Replace existing task if any
        workRequest
    )
}





class TokenRefreshWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val androidCtx = context

    override suspend fun doWork(): Result {
        return try {
            // Perform the token refresh logic
//            withContext(Dispatchers.IO) {
//
//            }
            val result = refreshToken(androidCtx)
            if (result) Result.success() else Result.retry()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // Retry if there's a failure
        }
    }



}
