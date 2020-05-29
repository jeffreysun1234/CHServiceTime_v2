package com.mycompany.chservicetime.timeslotdetail

sealed class TimeslotDetailResult {

    // data class Success(val vehicle: Vehicle) : TimeslotDetailResult()
    object CreatedSuccess : TimeslotDetailResult()
    object UpdatedSuccess : TimeslotDetailResult()
    object DeletedSuccess : TimeslotDetailResult()
}