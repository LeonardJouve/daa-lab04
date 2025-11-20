package ch.heigvd.iict.daa.template.views

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.template.App
import ch.heigvd.iict.daa.template.R
import ch.heigvd.iict.daa.template.database.entities.Note
import ch.heigvd.iict.daa.template.database.entities.NoteAndSchedule
import ch.heigvd.iict.daa.template.database.entities.Schedule
import ch.heigvd.iict.daa.template.database.entities.State
import ch.heigvd.iict.daa.template.database.entities.Type
import ch.heigvd.iict.daa.template.models.NotesViewModel
import ch.heigvd.iict.daa.template.models.NotesViewModelFactory
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {
    private val notesViewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory((requireActivity().application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    class MyRecyclerAdapter(_items : List<NoteAndSchedule> = listOf()) : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val title = view.findViewById<TextView>(R.id.list_note_title)
            private val scheduledTitle = view.findViewById<TextView>(R.id.list_scheduled_note_title)
            private val text = view.findViewById<TextView>(R.id.list_note_text)
            private val scheduledText = view.findViewById<TextView>(R.id.list_scheduled_note_text)
            private val icon = view.findViewById<ImageView>(R.id.list_note_icon)
            private val scheduledIcon = view.findViewById<ImageView>(R.id.list_scheduled_note_icon)
            private val scheduleIcon = view.findViewById<ImageView>(R.id.list_scheduled_note_schedule_icon)
            private val scheduleText = view.findViewById<TextView>(R.id.list_scheduled_note_schedule_text)

            fun getScheduleText(context: Context, scheduledDate: Calendar): String {
                val now = Calendar.getInstance()

                val diffMillis = scheduledDate.timeInMillis - now.timeInMillis
                val diffDays = (diffMillis / (24 * 60 * 60 * 1000)).toInt()

                return when {
                    diffMillis < 0 -> context.getString(R.string.late)
                    diffDays == 0 -> context.getString(R.string.today)
                    diffDays < 7 -> context.resources.getQuantityString(R.plurals.days, diffDays, diffDays)
                    diffDays < 30 -> {
                        val weeks = diffDays / 7
                        context.resources.getQuantityString(R.plurals.weeks, weeks, weeks)
                    }
                    diffDays < 365 -> {
                        val months = diffDays / 30
                        context.resources.getQuantityString(R.plurals.months, months, months)
                    }
                    else -> {
                        val years = diffDays / 365
                        context.resources.getQuantityString(R.plurals.years, years, years)
                    }
                }
            }


            fun bind(noteAndSchedule : NoteAndSchedule) {
                val iconId = when (noteAndSchedule.note.type) {
                    Type.NONE -> R.drawable.note
                    Type.TODO -> R.drawable.check_list
                    Type.SHOPPING -> R.drawable.basket
                    Type.WORK -> R.drawable.suitcase
                    Type.FAMILY -> R.drawable.family
                }
                val iconColor = if (noteAndSchedule.note.state == State.IN_PROGRESS) R.color.md_green_600 else R.color.md_blue_grey_600

                if(noteAndSchedule.schedule == null){
                    title.text = noteAndSchedule.note.title
                    text.text = noteAndSchedule.note.text
                    icon.setImageResource(iconId)
                    icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, iconColor))
                } else {
                    scheduledTitle.text = noteAndSchedule.note.title
                    scheduledText.text = noteAndSchedule.note.text
                    scheduledIcon.setImageResource(iconId)
                    scheduledIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, iconColor))
                    scheduleText.text = getScheduleText(itemView.context, noteAndSchedule.schedule.date)
                    val scheduleIconColor = if (noteAndSchedule.schedule.date.timeInMillis - Calendar.getInstance().timeInMillis < 0) R.color.md_red_600 else R.color.md_blue_grey_600
                    scheduleIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, scheduleIconColor))
                }
            }
        }

        inner class NotesDiffCallback(private val oldList: List<NoteAndSchedule>, private val newList: List<NoteAndSchedule>) : DiffUtil.Callback() {
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
                val diffCallback = NotesDiffCallback(items, value)
                val diffItems = DiffUtil.calculateDiff(diffCallback)
                field = value
                diffItems.dispatchUpdatesTo(this) // à la place de notifyDatasetChanged()
            }
        init { items = _items }
        override fun getItemCount() = items.size
        override fun getItemViewType(position: Int): Int {
            return if (items[position].schedule == null) NOTE else NOTE_WITH_SCHEDULE
        }
        private val NOTE_WITH_SCHEDULE = 1
        private val NOTE = 2
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
        notesViewModel.allNotes.observe(viewLifecycleOwner) {value ->
            adapter.items = sort(value, notesViewModel.sortType.value!!)
        }
        notesViewModel.sortType.observe(viewLifecycleOwner) { value ->
            adapter.items = sort(adapter.items, value)
        }
    }

    fun sort(items: List<NoteAndSchedule>, sortType: NotesViewModel.SortType): List<NoteAndSchedule> {
        return when (sortType) {
            NotesViewModel.SortType.CREATION_DATE ->
                items.sortedBy { it.note.creationDate }
            NotesViewModel.SortType.ETA ->
                items.sortedWith(
                    compareBy<NoteAndSchedule> { it.schedule == null }
                        .thenBy { it.schedule?.date }
                )
        }
    }
}