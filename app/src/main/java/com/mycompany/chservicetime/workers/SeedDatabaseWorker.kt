package com.mycompany.chservicetime.workers

import InitData
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mycompany.chservicetime.data.source.local.CHDatabase
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            val database: CHDatabase by inject()
            database.timeslotDao().insertTimeslot(InitData.timeslotEntity_1)
            database.timeslotDao().insertTimeslot(InitData.timeslotEntity_2)
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = SeedDatabaseWorker::class.java.simpleName
    }
}