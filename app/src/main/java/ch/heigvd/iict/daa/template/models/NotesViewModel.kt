package ch.heigvd.iict.daa.template.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.daa.template.database.Repository
import ch.heigvd.iict.daa.template.database.entities.Note
import ch.heigvd.iict.daa.template.database.entities.NoteAndSchedule
import ch.heigvd.iict.daa.template.database.entities.Schedule
import ch.heigvd.iict.daa.template.database.entities.State
import ch.heigvd.iict.daa.template.database.entities.Type
import java.util.Calendar
import kotlin.random.Random

class NotesViewModel(private val repository: Repository) : ViewModel() {
    enum class SortType { CREATION_DATE, ETA }
    private val _sortType = MutableLiveData<SortType>(SortType.CREATION_DATE)

    fun setSortType(sort: SortType) {
        _sortType.postValue(sort)
    }

    val allNotes = repository.allNotes
    val countNotes = repository.noteCount
    val sortType: LiveData<SortType> = _sortType

    val titles = arrayOf(
        "Meeting Reminder",
        "Grocery List",
        "Workout Plan",
        "Project Idea",
        "Birthday Reminder"
    )

    val texts = arrayOf(
        "Don't forget to buy milk and eggs.",
        "Finish the Kotlin project by tomorrow.",
        "Gym session at 6 PM.",
        "Call mom after work.",
        "Plan surprise party for John."
    )

    fun generateANote() {
        val note = Note(
            noteId = null,
            state = State.entries.toTypedArray().random(),
            title = titles.random(),
            text = texts.random(),
            creationDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, Random.nextInt(-30, 30))
                add(Calendar.HOUR_OF_DAY, Random.nextInt(0, 24))
                add(Calendar.MINUTE, Random.nextInt(0, 60))
            },
            type = Type.entries.toTypedArray().random(),
        )

        val schedule = if (Random.nextBoolean()) null else Schedule(
                scheduleId = null,
                ownerId = 0,
                date = Calendar.getInstance().apply {
                    // Random date within +-30 days from now
                    add(Calendar.DAY_OF_YEAR, Random.nextInt(-30, 30))
                    add(Calendar.HOUR_OF_DAY, Random.nextInt(0, 24))
                    add(Calendar.MINUTE, Random.nextInt(0, 60))
                }
        )
        val noteAndSchedule = NoteAndSchedule(note, schedule)


        repository.insertNoteAndSchedule(noteAndSchedule)
    }
    fun deleteAllNote() {
        repository.deleteAll()
    }
}

class NotesViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}