package com.mycompany.servicetime

import androidx.room.Room
import com.mycompany.servicetime.data.source.local.CHDatabase
import org.koin.dsl.module

val testLocalDataSourceModule = module(override = true) {
    // Room Database
    single {
        Room.inMemoryDatabaseBuilder(get(), CHDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    // single { InstrumentationRegistry.getInstrumentation().targetContext }
}

val testAppModules = listOf(testLocalDataSourceModule)