package jmapps.addmyfavoriteword.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.presentation.ui.holders.NoteItemsHolder

class NoteItemsAdapter(
    private val context: Context,
    private var noteItemList: MutableList<NoteItems>,
    private val onItemClickNote: OnItemClickNote,
    private val onLongClickNoteItem: OnLongClickNoteItem): RecyclerView.Adapter<NoteItemsHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstNoteItemList: MutableList<NoteItems>? = null

    init {
        this.firstNoteItemList = noteItemList
    }

    interface OnItemClickNote {
        fun onItemClickNote(noteId: Long, noteTitle: String, noteContent: String, noteColor: String, notePriority: String)
    }

    interface OnLongClickNoteItem {
        fun itemClickRenameNote(noteId: Long, noteTitle: String, noteContent: String, noteColor: String, notePriority: String)

        fun itemClickDeleteNote(noteId: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemsHolder {
        val viewItem = inflater.inflate(R.layout.item_note, parent, false)
        return (NoteItemsHolder(viewItem))
    }

    override fun onBindViewHolder(holder: NoteItemsHolder, position: Int) {
        val current = noteItemList[position]

        holder.tvNoteItemColor.setBackgroundColor(Color.parseColor(current.noteColor))
        holder.tvNoteItemColor.text = (position + 1).toString()

        val priorityName = arrayListOf("#FFFFFF", "#FFECB3", "#C8E6C9", "#FFCDD2")
        holder.tvNoteItemLayout.setBackgroundColor(Color.parseColor(priorityName[current.priority.toInt()]))

        if (current.noteTitle.isNotEmpty()) {
            holder.tvNoteItemTitle.text = current.noteTitle
        } else {
            holder.tvNoteItemTitle.text = "Без названия"
        }

        holder.tvNoteItemAddDateTime.text = current.addDateTime
        holder.tvNoteItemContent.text = current.noteContent
    }

    override fun getItemCount() = noteItemList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                noteItemList = if (charString.isEmpty()) {
                    firstNoteItemList as MutableList<NoteItems>
                } else {
                    val filteredList = ArrayList<NoteItems>()
                    for (row in firstNoteItemList!!) {
                        if (row.noteTitle.toLowerCase().contains(charString.toLowerCase()) ||
                            row.noteContent.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = noteItemList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                noteItemList = filterResults.values as ArrayList<NoteItems>
                notifyDataSetChanged()
            }
        }
    }
}