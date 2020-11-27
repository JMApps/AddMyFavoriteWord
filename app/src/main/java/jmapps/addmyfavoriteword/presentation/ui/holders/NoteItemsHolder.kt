package jmapps.addmyfavoriteword.presentation.ui.holders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R

class NoteItemsHolder(noteView: View) : RecyclerView.ViewHolder(noteView) {

    val tvNoteItemColor: TextView = noteView.findViewById(R.id.text_note_item_color)
    val tvNoteItemLayout: LinearLayoutCompat = noteView.findViewById(R.id.layout_note_priority)
    val tvNoteItemTitle: TextView = noteView.findViewById(R.id.text_title_note_item)
    val tvNoteItemAddDateTime: TextView = noteView.findViewById(R.id.text_view_note_item_add_date_time)
    val tvNoteItemContent: TextView = noteView.findViewById(R.id.text_view_note_item_little_content)
}