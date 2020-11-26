package jmapps.addmyfavoriteword.presentation.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.databinding.ActivityAddNoteBinding
import jmapps.addmyfavoriteword.presentation.ui.models.NotesItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddNoteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesItemViewModel: NotesItemViewModel

    private var standardNoteColor = "#ef5350"

    override fun onCreate(savedInstanceState: Bundle?) {
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.noteItemContent.textCurrentNoteColor.setOnClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_ready -> {
                addNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNote() {
        val insertNote = NoteItems(
            0,
            binding.noteItemContent.editAddNoteItemTitle.text.toString(),
            binding.noteItemContent.editAddNoteItemContent.text.toString(),
            standardNoteColor,
            MainOther().currentTime,
            MainOther().currentTime,
            binding.noteItemContent.spinnerNotePriority.selectedItemId
        )
        notesItemViewModel.insertNoteItem(insertNote)
    }

    override fun onClick(v: View?) {
        MaterialColorPickerDialog
            .Builder(this)
            .setTitle(getString(R.string.description_choose_color))
            .setColorRes(resources.getIntArray(R.array.themeColors).toList())
            .setColorListener { _, colorHex ->
                binding.noteItemContent.textCurrentNoteColor.setBackgroundColor(Color.parseColor(colorHex))
                standardNoteColor = colorHex
            }
            .show()
    }
}