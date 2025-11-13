package ch.heigvd.iict.daa.template

import android.app.Application
import ch.heigvd.iict.daa.template.database.MyDatabase
import ch.heigvd.iict.daa.template.database.Repository

class App: Application() {
    val repository by lazy {
        val database = MyDatabase.getDatabase(this)
        Repository(database.noteDAO(), database.scheduleDAO())
    }
}