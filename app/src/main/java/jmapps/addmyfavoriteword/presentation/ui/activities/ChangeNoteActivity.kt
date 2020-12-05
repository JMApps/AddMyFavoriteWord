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
import jmapps.addmyfavoriteword.databinding.ActivityChangeNoteBinding
import jmapps.addmyfavoriteword.presentation.ui.models.NotesItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.QuestionAlertUtil

class ChangeNoteActivity : AppCompatActivity(), QuestionAlertUtil.OnClickQuestion,
    View.OnClickListener, TextWatcher {

    private lateinit var binding: ActivityChangeNoteBinding
    private lateinit var notesItemViewModel: NotesItemViewModel

    private lateinit var questionAlertUtil: QuestionAlertUtil
    private var itemChangeNote: MenuItem? = null

    private var currentNoteId: Long? = null
    private var currentNoteColor: String? = null
    private var currentNotePriority: Long? = null
    private var currentNoteTitle: String? = null
    private var currentNoteContent: String? = null

    companion object {
        private const val KEY_RESTORE_CURRENT_NOTE_COLOR = "key_restore_current_note_color"
        private const val KEY_CURRENT_NOTE_ID = "key_current_note_id"
        private const val KEY_CURRENT_NOTE_COLOR = "key_current_note_color"
        private const val KEY_CURRENT_NOTE_PRIORITY = "key_current_note_priority"
        private const val KEY_CURRENT_NOTE_TITLE = "key_current_note_title"
        private const val KEY_CURRENT_NOTE_CONTENT = "key_current_note_content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_note)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentNoteId = intent.getLongExtra(KEY_CURRENT_NOTE_ID, 0)
        currentNoteColor = intent.getStringExtra(KEY_CURRENT_NOTE_COLOR)
        currentNotePriority = intent.getLongExtra(KEY_CURRENT_NOTE_PRIORITY, 0)
        currentNoteTitle = intent.getStringExtra(KEY_CURRENT_NOTE_TITLE)
        currentNoteContent = intent.getStringExtra(KEY_CURRENT_NOTE_CONTENT)

        DrawableCompat.setTint(binding.changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(currentNoteColor))

        questionAlertUtil = QuestionAlertUtil(this, this)

        val noteTitleCharacters = getString(R.string.max_note_title_characters, 0)
        binding.changeNoteItemContent.textLengthChangeNoteCharacters.text = noteTitleCharacters

        binding.changeNoteItemContent.textChangeNoteColor.setOnClickListener(this)
        binding.changeNoteItemContent.editChangeNoteItemTitle.addTextChangedListener(this)
        binding.changeNoteItemContent.editChangeNoteItemContent.addTextChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        itemChangeNote = menu!!.findItem(R.id.action_add_note)
        itemChangeNote?.isVisible = checkEditTexts()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (checkEditTexts()) {
            if (checkInitialNewValues()) {
                questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_change_item_note_without_save))
            } else {
                changeNote()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_RESTORE_CURRENT_NOTE_COLOR, currentNoteColor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNoteColor = savedInstanceState.getString(KEY_RESTORE_CURRENT_NOTE_COLOR).toString()
        DrawableCompat.setTint(binding.changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(currentNoteColor))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (checkEditTexts()) {
                    if (checkInitialNewValues()) {
                        questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_change_item_note_without_save))
                    } else {
                        changeNote()
                    }
                } else {
                    finish()
                }
            }
            R.id.action_change_note -> {
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
                currentNoteColor = colorHex
                DrawableCompat.setTint(binding.changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(currentNoteColor))
            }
            .show()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (binding.changeNoteItemContent.editChangeNoteItemTitle.text.hashCode() == s?.hashCode()) {
            if (s.length <= 200) {
                val noteTitleCharacters = getString(R.string.max_note_title_characters, s.length)
                binding.changeNoteItemContent.textLengthChangeNoteCharacters.text = noteTitleCharacters
            }

            if (s.length == 200) {
                Toast.makeText(this, getString(R.string.toast_achieved_max_note_title_characters), Toast.LENGTH_SHORT).show()
            }
        }
        itemChangeNote?.isVisible = checkEditTexts()
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClickPositive() {
        assignNewValues()
        changeNote()
    }

    override fun onClickDelete() {
        Toast.makeText(this, getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun checkEditTexts(): Boolean {
        return binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString().isNotEmpty() ||
                binding.changeNoteItemContent.editChangeNoteItemContent.text.toString().isNotEmpty()
    }

    private fun checkInitialNewValues(): Boolean {
        return currentNoteTitle != binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString() ||
                currentNoteContent != binding.changeNoteItemContent.editChangeNoteItemContent.text.toString()
    }

    private fun assignNewValues () {
        currentNoteTitle = binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString()
        currentNoteContent = binding.changeNoteItemContent.editChangeNoteItemContent.text.toString()
        itemChangeNote?.isVisible = false
    }

    private fun clearFocus() {
        binding.changeNoteItemContent.editChangeNoteItemTitle.clearFocus()
        binding.changeNoteItemContent.editChangeNoteItemContent.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        binding.changeNoteItemContent.editChangeNoteItemContent.postDelayed(Runnable {
            val keyboard: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.changeNoteItemContent.editChangeNoteItemContent.windowToken, 0)
        }, 200)
    }

    private fun changeNote() {
        notesItemViewModel.updateNoteItem(
            currentNoteTitle!!,
            currentNoteContent!!,
            currentNoteColor!!,
            currentNotePriority!!,
            currentNoteId!!
        )
        Toast.makeText(this, getString(R.string.toast_note_changed), Toast.LENGTH_SHORT).show()
        finish()
    }
}