package ch.heigvd.iict.daa.template.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heigvd.iict.daa.template.database.dao.NoteDAO
import ch.heigvd.iict.daa.template.database.entities.Note
import java.util.Calendar
import java.util.Date
import kotlin.concurrent.thread


class CalendarConverter {
    @TypeConverter
    fun toCalendar(dateLong: Long) =
        Calendar.getInstance().apply {
            time = Date(dateLong)
        }
    @TypeConverter
    fun fromCalendar(date: Calendar) =
        date.time.time
}

@Database(entities = [Note::class],
    version = 1,
    exportSchema = true)
@TypeConverters(CalendarConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun noteDAO() : NoteDAO
    companion object {
        private var INSTANCE : MyDatabase? = null
        fun getDatabase(context: Context) : MyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    MyDatabase::class.java, "mydatabase.db")
                    .addCallback(MyDatabaseCallBack())
                    .build()
                INSTANCE!!
            }
        }

        private class MyDatabaseCallBack : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {database ->
                    thread {
                        val isEmpty = database.noteDAO().getCount().value == 0L
                        if (isEmpty) {
                            // TODO seed asynchronously
                        }
                    }
                }
            }
            override fun onOpen(db: SupportSQLiteDatabase) { super.onOpen(db) }
            override fun onDestructiveMigration(db: SupportSQLiteDatabase) { super.onDestructiveMigration(db) }
        }
    }
}