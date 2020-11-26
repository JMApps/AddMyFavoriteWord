package jmapps.addmyfavoriteword.presentation.ui.holders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R

class NoteItemsHolder(noteView: View) : RecyclerView.ViewHolder(noteView) {

    val noteItemColor: TextView = noteView.findViewById(R.id.text_note_item_color)
    val noteItemLayout: LinearLayoutCompat = noteView.findViewById(R.id.layout_note_priority)
    val noteItemTitle: TextView = noteView.findViewById(R.id.text_title_note_item)
    val noteItemAddDateTime: TextView = noteView.findViewById(R.id.text_view_note_item_add_date_time)
    val noteItemContent: TextView = noteView.findViewById(R.id.text_view_note_item_little_content)
}