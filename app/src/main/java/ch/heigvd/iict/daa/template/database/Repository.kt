package ch.heigvd.iict.daa.template.database

import ch.heigvd.iict.daa.template.database.dao.NoteDAO
import ch.heigvd.iict.daa.template.database.dao.ScheduleDAO
import ch.heigvd.iict.daa.template.database.entities.Note
import ch.heigvd.iict.daa.template.database.entities.NoteAndSchedule
import ch.heigvd.iict.daa.template.database.entities.Schedule
import kotlin.concurrent.thread

class Repository(private val noteDAO: NoteDAO, private val scheduleDAO: ScheduleDAO) {
    val allNotes = noteDAO.getAllNotes()
    val noteCount = noteDAO.getCount()

    fun insertNoteAndSchedule(noteAndSchedule: NoteAndSchedule) {
        thread {
            val id = noteDAO.insert(noteAndSchedule.note)
            if (noteAndSchedule.schedule != null) {
                noteAndSchedule.schedule.ownerId = id
                scheduleDAO.insert(noteAndSchedule.schedule)
            }
        }
    }

    fun deleteAll() {
        thread {
            scheduleDAO.deleteAll()
            noteDAO.deleteAll()
        }
    }
}