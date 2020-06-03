package com.mycompany.chservicetime.timeslotlist

sealed class TimeslotListResult {

    data class Title(val resTitle: Int) : TimeslotListResult()
    object NeedDNDPermission : TimeslotListResult()
}