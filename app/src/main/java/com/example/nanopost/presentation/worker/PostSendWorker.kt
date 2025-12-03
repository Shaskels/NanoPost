package com.example.nanopost.presentation.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.nanopost.R
import com.example.nanopost.domain.usecase.UploadPostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

@HiltWorker
class PostSendWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val uploadPostUseCase: UploadPostUseCase
) : CoroutineWorker(appContext, workerParams) {

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "uploading"
        private const val KEY_TEXT = "text"
        private const val KEY_IMAGES = "images"

        fun newRequest(text: String?, images: List<Uri>) =
            OneTimeWorkRequestBuilder<PostSendWorker>()
                .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    Data.Builder()
                        .putString(KEY_TEXT, text)
                        .putStringArray(KEY_IMAGES, images.map { it.toString() }.toTypedArray())
                        .build()
                )
                .build()
    }

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {
        val text = inputData.getString(KEY_TEXT)
        val imageUris = inputData.getStringArray(KEY_IMAGES)?.map { it.toUri() }.orEmpty()

        if (text.isNullOrBlank() && imageUris.isEmpty()) {
            return Result.failure()
        }

        try {
            setForeground(getForegroundInfo())
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
        }

        uploadPostUseCase(text, imageUris)
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private fun createNotification(): Notification {
        createChannel()

        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_screen)
            .setContentTitle(applicationContext.getString(R.string.post_is_uploading))
            .setOngoing(true)
            .setProgress(0, 0, true)
            .addAction(R.drawable.cancel, "Cancel", intent)
            .build()
    }

    private fun createChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, "Data upload", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }
}