package com.mycompany.chservicetime.timeslotdetail

sealed class TimeslotDetailResult {

    data class Title(val resTitle: Int) : TimeslotDetailResult()
    object CreatedSuccess : TimeslotDetailResult()
    object UpdatedSuccess : TimeslotDetailResult()
    object DeletedSuccess : TimeslotDetailResult()
}