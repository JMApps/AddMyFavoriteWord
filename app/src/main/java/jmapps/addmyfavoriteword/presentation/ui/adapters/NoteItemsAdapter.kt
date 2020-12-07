package jmapps.addmyfavoriteword.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.presentation.ui.holders.NoteItemsHolder

class NoteItemsAdapter(
    context: Context,
    private var noteItemList: MutableList<NoteItems>,
    private val onItemClickNote: OnItemClickNote,
    private val onLongItemClickNote: OnLongItemClickNote): RecyclerView.Adapter<NoteItemsHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstNoteItemList: MutableList<NoteItems>? = null

    init {
        this.firstNoteItemList = noteItemList
    }

    interface OnItemClickNote {
        fun onItemClickNote(noteId: Long, noteColor: String, notePriority: Long, noteTitle: String, noteContent: String)
    }

    interface OnLongItemClickNote {
        fun onLongItemClickNote(noteId: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemsHolder {
        val viewItem = inflater.inflate(R.layout.item_note, parent, false)
        return (NoteItemsHolder(viewItem))
    }

    override fun onBindViewHolder(holder: NoteItemsHolder, position: Int) {
        val current = noteItemList[position]

        holder.tvNoteItemLayout.setBackgroundColor(Color.parseColor(current.noteColor))

        val priorityName = arrayListOf("#FFFFFF", "#F4FF81", "#00C853", "#FF3D00")
        DrawableCompat.setTint(holder.tvNoteItemPriority.background, Color.parseColor(priorityName[current.priority.toInt()]))

        if (current.noteTitle.isNotEmpty()) {
            holder.tvNoteItemTitle.text = current.noteTitle
        } else {
            holder.tvNoteItemTitle.text = "Без названия"
        }

        holder.tvNoteItemAddDateTime.text = current.addDateTime
        holder.tvNoteItemContent.text = current.noteContent

        holder.findNoteItemClick(onItemClickNote, current._id, current.noteColor, current.priority, current.noteTitle, current.noteContent)
        holder.findNoteLongItemClick(onLongItemClickNote, current._id)
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