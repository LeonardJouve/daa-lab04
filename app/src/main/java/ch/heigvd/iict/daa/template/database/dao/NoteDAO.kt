package ch.heigvd.iict.daa.template.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ch.heigvd.iict.daa.template.database.entities.Note
import ch.heigvd.iict.daa.template.database.entities.NoteAndSchedule

@Dao
interface NoteDAO {
    @Insert
    fun insert(note : Note) : Long

    @Transaction
    @Query("SELECT * FROM Note")
    fun getAllNotes() : LiveData<List<NoteAndSchedule>>

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM Note")
    fun getCount() : LiveData<Long>
}