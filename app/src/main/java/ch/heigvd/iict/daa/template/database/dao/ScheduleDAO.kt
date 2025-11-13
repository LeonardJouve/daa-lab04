package ch.heigvd.iict.daa.template.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ch.heigvd.iict.daa.template.database.entities.Schedule

@Dao
interface ScheduleDAO {
    @Insert
    fun insert(schedule: Schedule) : Long

    @Query("SELECT * FROM Schedule")
    fun getAllNotes() : LiveData<List<Schedule>>
    @Query("DELETE FROM Schedule")
    fun deleteAll()
}