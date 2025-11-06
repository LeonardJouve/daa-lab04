package ch.heigvd.iict.daa.template.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ch.heigvd.iict.daa.template.database.entities.Note

@Dao
interface NoteDAO {
    @Insert
    fun insert(note : Note) : Long
    @Update
    fun update(note : Note)
    @Delete
    fun delete(note : Note)
    @Query("SELECT * FROM Note")
    fun getAllNotes() : LiveData<List<Note>>

    @Insert
    fun insertAll(vararg notes : Note)

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM Note")
    fun getCount() : LiveData<Long>
}