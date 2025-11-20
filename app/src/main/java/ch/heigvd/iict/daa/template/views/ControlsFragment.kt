package ch.heigvd.iict.daa.template.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.heigvd.iict.daa.template.App
import ch.heigvd.iict.daa.template.R
import ch.heigvd.iict.daa.template.models.NotesViewModel
import ch.heigvd.iict.daa.template.models.NotesViewModelFactory

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [ControlsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlsFragment : Fragment() {
    private val notesViewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory((requireActivity().application as App).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = view.findViewById<TextView>(R.id.note_count)
        val btnGenerate = view.findViewById<Button>(R.id.btn_generate)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        notesViewModel.countNotes.observe(viewLifecycleOwner) { count ->
            counter.text = count.toString()
        }

        btnGenerate.setOnClickListener { notesViewModel.generateANote() }
        btnDelete.setOnClickListener { notesViewModel.deleteAllNote() }
    }
}