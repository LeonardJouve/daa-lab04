package ch.heigvd.iict.daa.template

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ch.heigvd.iict.daa.template.models.NotesViewModel
import ch.heigvd.iict.daa.template.models.NotesViewModelFactory
import ch.heigvd.iict.daa.template.views.ControlsFragment
import ch.heigvd.iict.daa.template.views.NotesFragment

class MainActivity : AppCompatActivity() {
    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // depuis android 15 (sdk 35), le mode edge2edge doit être activé
        enableEdgeToEdge()

        // on spécifie le layout à afficher
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_notes, NotesFragment())
            .commit()

        val controlsContainer = findViewById<View?>(R.id.fragment_controls)
        if (controlsContainer != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_controls, ControlsFragment())
                .commit()
        }


        // comme edge2edge est activé, l'application doit garder un espace suffisant pour la barre système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // la barre d'action doit être définie dans le layout, on la lie à l'activité
        setSupportActionBar(findViewById(R.id.toolbar))

        // TODO ...

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_sort_creation_date -> {
                notesViewModel.setSortType(NotesViewModel.SortType.CREATION_DATE)
                true
            }

            R.id.menu_sort_eta -> {
                notesViewModel.setSortType(NotesViewModel.SortType.ETA)
                true
            }

            R.id.menu_actions_generate -> {
                notesViewModel.generateANote()
                true
            }

            R.id.menu_actions_delete_all -> {
                notesViewModel.deleteAllNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}