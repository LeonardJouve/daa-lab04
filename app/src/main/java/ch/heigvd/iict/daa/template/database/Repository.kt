package ch.heigvd.iict.daa.template.database

import ch.heigvd.iict.daa.template.database.dao.NoteDAO
import ch.heigvd.iict.daa.template.database.entities.Note
import kotlin.concurrent.thread

class Repository(private val noteDAO: NoteDAO) {
    val allNotes = noteDAO.getAllNotes()
    val noteCount = noteDAO.getCount()
    fun insertPersons(vararg notes : Note) {
        thread {
            //* est le spread operator, pour passer un array à une fonction acceptant un vararg
            noteDAO.insertAll(*notes)
        }
    }
    fun deleteAll() {
        thread {
            noteDAO.deleteAll()
        }
    }
}