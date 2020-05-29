package com.mycompany.chservicetime.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object for the timeslot table.
 */
@Dao
interface TimeslotDao {

    /**
     * Select all timeslots from the timeslots table.
     *
     * @return all timeslots.
     */
    @Query("SELECT * FROM timeslots")
    fun getTimeslotList(): LiveData<List<TimeslotEntity>>

    /**
     * Select a timeslot by id.
     *
     * @param timeslotId the timeslot id.
     * @return the timeslot with timeslotId.
     */
    @Query("SELECT * FROM Timeslots WHERE entry_id = :timeslotId")
    fun getTimeslotById(timeslotId: String): LiveData<TimeslotEntity>

    /**
     * Insert a timeslot in the database. If the timeslot already exists, replace it.
     *
     * @param timeslot the timeslot to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeslot(timeslotEntity: TimeslotEntity)

    /**
     * Update a timeslot.
     *
     * @param timeslot timeslot to be updated
     * @return the number of timeslots updated. This should always be 1.
     */
    @Update
    suspend fun updateTimeslot(timeslotEntity: TimeslotEntity): Int

    /**
     * Update the active status of a timeslot
     *
     * @param timeslotId    id of the timeslot
     * @param activated status to be updated
     */
    @Query("UPDATE timeslots SET activated = :activated WHERE entry_id = :timeslotId")
    suspend fun updateActivated(timeslotId: String, activated: Boolean)

    /**
     * Delete a timeslot by id.
     *
     * @return the number of timeslots deleted. This should always be 1.
     */
    @Query("DELETE FROM Timeslots WHERE entry_id = :timeslotId")
    suspend fun deleteTimeslotById(timeslotId: String): Int

    /**
     * Delete all timeslots.
     */
    @Query("DELETE FROM Timeslots")
    suspend fun deleteAllTimeslot()
}
