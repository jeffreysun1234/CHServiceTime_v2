package com.mycompany.servicetime.di

import com.mycompany.servicetime.data.source.DataRepository
import com.mycompany.servicetime.data.source.TimeslotRepository
import com.mycompany.servicetime.data.source.local.CHDatabase
import com.mycompany.servicetime.timeslotdetail.TimeslotDetailViewModel
import com.mycompany.servicetime.timeslotlist.TimeslotListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val localDataSourceModule = module {

    single<DataRepository> { TimeslotRepository(get()) }

    // Room Database
    single { CHDatabase.buildDatabase(androidApplication()) }

    // Expose TimeslotDao
    single { get<CHDatabase>().timeslotDao() }
}

val appModule = module {

    viewModel { TimeslotListViewModel(get(), get()) }
    viewModel { (timeslotId: String) -> TimeslotDetailViewModel(timeslotId, get()) }

}

val appModules = listOf(appModule, localDataSourceModule)