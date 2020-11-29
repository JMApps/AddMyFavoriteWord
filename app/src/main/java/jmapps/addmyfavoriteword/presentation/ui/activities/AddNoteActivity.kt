package jmapps.addmyfavoriteword.presentation.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.databinding.ActivityAddNoteBinding
import jmapps.addmyfavoriteword.presentation.ui.models.NotesItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddNoteActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesItemViewModel: NotesItemViewModel

    private var standardNoteColor = "#e57373"

    override fun onCreate(savedInstanceState: Bundle?) {
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        DrawableCompat.setTint(binding.noteItemContent.textCurrentNoteColor.background, Color.parseColor(standardNoteColor))

        val noteTitleCharacters = getString(R.string.max_note_title_characters, 0)
        binding.noteItemContent.textLengthAddNoteCharacters.text = noteTitleCharacters

        binding.noteItemContent.textCurrentNoteColor.setOnClickListener(this)
        binding.noteItemContent.editAddNoteItemTitle.addTextChangedListener(this)

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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val noteTitleCharacters = getString(R.string.max_note_title_characters, s?.length)
        binding.noteItemContent.textLengthAddNoteCharacters.text = noteTitleCharacters
    }

    override fun afterTextChanged(s: Editable?) {}
}