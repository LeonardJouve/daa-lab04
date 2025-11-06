package ch.heigvd.iict.daa.template.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true) var noteId : Long?,
    var state : State,
    var title : String,
    var text : String,
    var creationDate : Calendar,
    var type : Type
)

enum class State { IN_PROGRESS, DONE }

enum class Type { NONE, TODO, SHOPPING, WORK, FAMILY }