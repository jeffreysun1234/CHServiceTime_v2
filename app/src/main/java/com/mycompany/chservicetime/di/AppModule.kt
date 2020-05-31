package com.mycompany.chservicetime.di

import com.mycompany.chservicetime.data.source.TimeslotRepository
import com.mycompany.chservicetime.data.source.local.CHDatabase
import com.mycompany.chservicetime.timeslotdetail.TimeslotDetailViewModel
import com.mycompany.chservicetime.timeslotlist.TimeslotListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val localDataSourceModule = module {

    single { TimeslotRepository(get()) }

    // Room Database
    single { CHDatabase.buildDatabase(androidApplication()) }

    // Expose WeatherDAO
    single { get<CHDatabase>().timeslotDao() }
}

val appModule = module {

    viewModel { TimeslotListViewModel(get(), get()) }
    viewModel { (timeslotId: String) -> TimeslotDetailViewModel(timeslotId, get()) }

}

val appModules = listOf(appModule, localDataSourceModule)