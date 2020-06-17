package com.mycompany.servicetime.workers

import InitData
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mycompany.servicetime.data.source.local.CHDatabase
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

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
            Timber.e(ex, "Error seeding database")
            Result.failure()
        }
    }

    companion object {
        private val TAG = SeedDatabaseWorker::class.java.simpleName
    }
}