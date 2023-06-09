package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import timber.log.Timber
import java.io.File

/**
 * Cleans up temporary files generated during blurring process
 */

class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temporary files", applicationContext,id)
        sleep()
        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()){
                val entries = outputDirectory.listFiles()
                if (entries !=null) {
                    for (entru in entries) {
                        val name = entru.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entru.delete()
                            Timber.i("Deleted " + name + " - " + deleted)
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}