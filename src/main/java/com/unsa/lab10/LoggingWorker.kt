package com.unsa.lab10

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LoggingWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        Log.d("LoggingWorker", "Proceso ejecutado en segundo plano (Hora: ${time})")
        delay(15000)
        scheduleNext(applicationContext)
        return Result.success()
    }
    companion object {
        fun scheduleNext(context: Context) {
            val request = OneTimeWorkRequestBuilder<LoggingWorker>()
                .addTag("logging_worker")
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}