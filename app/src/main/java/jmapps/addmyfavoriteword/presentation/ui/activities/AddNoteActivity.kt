package jmapps.addmyfavoriteword.presentation.ui.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import jmapps.addmyfavoriteword.presentation.ui.other.QuestionAlertUtil

class AddNoteActivity : AppCompatActivity(), View.OnClickListener, QuestionAlertUtil.OnClickQuestion,
    TextWatcher {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var notesItemViewModel: NotesItemViewModel

    private var standardNoteColor = "#e57373"

    private lateinit var questionAlertUtil: QuestionAlertUtil
    private var itemAddNote: MenuItem? = null

    private var currentNoteTitle: String? = null
    private var currentNoteContent: String? = null

    companion object {
        private const val KEY_RESTORE_STANDARD_NOTE_COLOR = "key_restore_standard_note_color"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        DrawableCompat.setTint(binding.addNoteItemContent.textCurrentNoteColor.background, Color.parseColor(standardNoteColor))

        questionAlertUtil = QuestionAlertUtil(this, this)

        val noteTitleCharacters = getString(R.string.max_note_title_characters, 0)
        binding.addNoteItemContent.textLengthAddNoteCharacters.text = noteTitleCharacters

        binding.addNoteItemContent.textCurrentNoteColor.setOnClickListener(this)
        binding.addNoteItemContent.editAddNoteItemTitle.addTextChangedListener(this)
        binding.addNoteItemContent.editAddNoteItemContent.addTextChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        itemAddNote = menu!!.findItem(R.id.action_add_note)
        itemAddNote?.isVisible = checkEditTexts()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (checkEditTexts()) {
            if (checkInitialNewValues()) {
                questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_add_item_note_without_save))
            } else {
                addNote()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_RESTORE_STANDARD_NOTE_COLOR, standardNoteColor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        standardNoteColor = savedInstanceState.getString(KEY_RESTORE_STANDARD_NOTE_COLOR).toString()
        DrawableCompat.setTint(binding.addNoteItemContent.textCurrentNoteColor.background, Color.parseColor(standardNoteColor))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (checkEditTexts()) {
                    if (checkInitialNewValues()) {
                        questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_add_item_note_without_save))
                    } else {
                        addNote()
                    }
                } else {
                    finish()
                }
            }
            R.id.action_add_note -> {
                if (checkEditTexts()) {
                    assignNewValues()
                    clearFocus()
                } else {
                    Toast.makeText(this, getString(R.string.action_canceled), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        MaterialColorPickerDialog
            .Builder(this)
            .setTitle(getString(R.string.description_choose_color))
            .setColorRes(resources.getIntArray(R.array.themeColors).toList())
            .setColorListener { _, colorHex ->
                standardNoteColor = colorHex
                DrawableCompat.setTint(binding.addNoteItemContent.textCurrentNoteColor.background, Color.parseColor(standardNoteColor))
            }
            .show()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (binding.addNoteItemContent.editAddNoteItemTitle.text.hashCode() == s?.hashCode()) {
            if (s.length <= 200) {
                val noteTitleCharacters = getString(R.string.max_note_title_characters, s.length)
                binding.addNoteItemContent.textLengthAddNoteCharacters.text = noteTitleCharacters
            }

            if (s.length == 200) {
                Toast.makeText(this, getString(R.string.toast_achieved_max_note_title_characters), Toast.LENGTH_SHORT).show()
            }
        }
        itemAddNote?.isVisible = checkEditTexts()
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClickPositive() {
        assignNewValues()
        addNote()
    }

    override fun onClickDelete() {
        Toast.makeText(this, getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun checkEditTexts(): Boolean {
        return binding.addNoteItemContent.editAddNoteItemTitle.text.toString().isNotEmpty() ||
                binding.addNoteItemContent.editAddNoteItemContent.text.toString().isNotEmpty()
    }

    private fun checkInitialNewValues(): Boolean {
        return currentNoteTitle != binding.addNoteItemContent.editAddNoteItemTitle.text.toString() ||
                currentNoteContent != binding.addNoteItemContent.editAddNoteItemContent.text.toString()
    }

    private fun assignNewValues () {
        currentNoteTitle = binding.addNoteItemContent.editAddNoteItemTitle.text.toString()
        currentNoteContent = binding.addNoteItemContent.editAddNoteItemContent.text.toString()
        itemAddNote?.isVisible = false
    }

    private fun clearFocus() {
        binding.addNoteItemContent.editAddNoteItemTitle.clearFocus()
        binding.addNoteItemContent.editAddNoteItemContent.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        binding.addNoteItemContent.editAddNoteItemContent.postDelayed(Runnable {
            val keyboard: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.addNoteItemContent.editAddNoteItemContent.windowToken, 0)
        }, 200)
    }

    private fun addNote() {
        val insertNote = NoteItems(
            0,
            currentNoteTitle!!.trim(),
            currentNoteContent!!.trim(),
            standardNoteColor,
            MainOther().currentTime,
            MainOther().currentTime,
            binding.addNoteItemContent.spinnerNotePriority.selectedItemId
        )
        notesItemViewModel.insertNoteItem(insertNote)
        Toast.makeText(this, getString(R.string.toast_note_added), Toast.LENGTH_SHORT).show()
        finish()
    }
}