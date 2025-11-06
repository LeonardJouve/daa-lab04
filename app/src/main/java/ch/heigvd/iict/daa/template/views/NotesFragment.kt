package ch.heigvd.iict.daa.template.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.template.R
import ch.heigvd.iict.daa.template.database.entities.Note
import ch.heigvd.iict.daa.template.database.entities.NoteAndSchedule
import ch.heigvd.iict.daa.template.database.entities.State
import ch.heigvd.iict.daa.template.database.entities.Type
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    class MyRecyclerAdapter(_items : List<NoteAndSchedule> = listOf()) : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val note = view.findViewById<TextView>(R.id.list_note_title)
            private val scheduledNote = view.findViewById<TextView>(R.id.list_scheduled_note_title)
            fun bind(noteAndSchedule : NoteAndSchedule) {
                if(noteAndSchedule.schedule == null){
                    note.text = noteAndSchedule.note.text
                    // scheduledNote.schedule = noteAndSchedule.text
                } else {
                    scheduledNote.text = noteAndSchedule.note.text
                }
            }
        }

        inner class AnimalsDiffCallback(private val oldList: List<NoteAndSchedule>, private val newList: List<NoteAndSchedule>) : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].note.noteId == newList.get(newItemPosition).note.noteId
            }
            override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int): Boolean {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]
                return old::class == new::class && old.note.title == new.note.title
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return when(viewType) {
                NOTE -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_note, parent, false))
                else /* NOTE_WITH_SCHEDULE */ -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_note_schedule, parent, false))
            }
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        var items = listOf<NoteAndSchedule>()
            set(value) {
                val diffCallback = AnimalsDiffCallback(items, value)
                val diffItems = DiffUtil.calculateDiff(diffCallback)
                field = value
                diffItems.dispatchUpdatesTo(this) // à la place de notifyDatasetChanged()
            }

                init { items = _items }
        override fun getItemCount() = items.size
        override fun getItemViewType(position: Int): Int {
            if(items[position].schedule == null)
                return NOTE
            else return NOTE_WITH_SCHEDULE
        }
        companion object {
            private val NOTE_WITH_SCHEDULE = 1
            private val NOTE = 2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_note)
        val adapter = MyRecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(view.context)
        adapter.items = listOf(NoteAndSchedule(Note(1,State.IN_PROGRESS,"bejrsr,","testts",
            Calendar.getInstance(), Type.SHOPPING),null))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotesFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            NotesFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}