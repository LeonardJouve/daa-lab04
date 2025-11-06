package ch.heigvd.iict.daa.template.models

import androidx.lifecycle.ViewModel
import ch.heigvd.iict.daa.template.database.Repository

class NotesViewModel(private val repository: Repository) : ViewModel() {
    val allNotes = repository.allNotes
    val countNotes = repository.noteCount

    fun generateANote() {
        // TODO
    }
    fun deleteAllNote() {
        // TODO
    }
}