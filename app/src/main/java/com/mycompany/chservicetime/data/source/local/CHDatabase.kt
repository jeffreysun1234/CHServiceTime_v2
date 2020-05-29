package com.mycompany.chservicetime.data.source.local

import DATABASE_NAME
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mycompany.chservicetime.workers.SeedDatabaseWorker

/**
 * The Room Database that contains the Timeslot table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [TimeslotEntity::class], version = 1, exportSchema = false)
abstract class CHDatabase : RoomDatabase() {

    abstract fun timeslotDao(): TimeslotDao

    companion object {

        fun buildDatabase(context: Context): CHDatabase {
            return Room.databaseBuilder(context, CHDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}
