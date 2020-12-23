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
import android.widget.AdapterView
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

    private var intermediateVariableNoteColor: String? = null

    private var newNoteColor: String? = null
    private var newNotePriority: Long? = null
    private var newNoteTitle: String? = null
    private var newNoteContent: String? = null

    companion object {
        private const val KEY_RESTORE_CURRENT_NOTE_COLOR = "key_restore_current_note_color"
        const val KEY_CURRENT_NOTE_ID = "key_current_note_id"
        const val KEY_CURRENT_NOTE_COLOR = "key_current_note_color"
        const val KEY_CURRENT_NOTE_PRIORITY = "key_current_note_priority"
        const val KEY_CURRENT_NOTE_TITLE = "key_current_note_title"
        const val KEY_CURRENT_NOTE_CONTENT = "key_current_note_content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_note)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        questionAlertUtil = QuestionAlertUtil(this, this)

        currentNoteId = intent.getLongExtra(KEY_CURRENT_NOTE_ID, 0)
        currentNoteColor = intent.getStringExtra(KEY_CURRENT_NOTE_COLOR)
        currentNotePriority = intent.getLongExtra(KEY_CURRENT_NOTE_PRIORITY, 0)
        currentNoteTitle = intent.getStringExtra(KEY_CURRENT_NOTE_TITLE)
        currentNoteContent = intent.getStringExtra(KEY_CURRENT_NOTE_CONTENT)

        intermediateVariableNoteColor = currentNoteColor

        binding.apply {
            DrawableCompat.setTint(changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(intermediateVariableNoteColor))
            changeNoteItemContent.spinnerNoteNewPriority.setSelection(currentNotePriority!!.toInt())
            if (!currentNoteTitle.isNullOrEmpty()) {
                changeNoteItemContent.editChangeNoteItemTitle.setText(currentNoteTitle)
            } else {
                changeNoteItemContent.editChangeNoteItemTitle.hint = "Без названия"
            }
            val currentNoteTitleCharacters = getString(R.string.max_note_title_characters, currentNoteTitle?.length)
            changeNoteItemContent.textLengthChangeNoteCharacters.text = currentNoteTitleCharacters
            changeNoteItemContent.editChangeNoteItemContent.setText(currentNoteContent)
        }

        binding.changeNoteItemContent.textChangeNoteColor.setOnClickListener(this)
        binding.changeNoteItemContent.spinnerNoteNewPriority.onItemSelectedListener = onItemSpinnerClick
        binding.changeNoteItemContent.editChangeNoteItemTitle.addTextChangedListener(this)
        binding.changeNoteItemContent.editChangeNoteItemContent.addTextChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_change_note, menu)
        itemChangeNote = menu!!.findItem(R.id.action_change_note)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        // Проверяем, изменились ли поступившие данные
        if (checkChangeCurrentValues()) {
            if (checkEditTexts()) {
                // Вызываем диалоговое окно с предложением применить изменение
                questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_change_item_note_without_save),
                    getString(R.string.alert_apply), getString(R.string.alert_not_apply))
            } else {
                Toast.makeText(this, getString(R.string.action_canceled), Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }
        } else {
            if (checkChangeNewValues()) {
                super.onBackPressed()
            } else {
                // Если данные были изменены
                assignNewValues()
                changeNote()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Проверяем, изменились ли поступившие данные
                if (checkChangeCurrentValues()) {
                    // Проверяем, не пустые ли поля ввода
                    if (checkEditTexts()) {
                        questionAlertUtil.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_change_item_note_without_save),
                            getString(R.string.alert_apply), getString(R.string.alert_not_apply))
                    } else {
                        Toast.makeText(this, getString(R.string.action_canceled), Toast.LENGTH_SHORT).show()
                        super.onBackPressed()
                    }
                } else {
                    if (checkChangeNewValues()) {
                        super.onBackPressed()
                    } else {
                        // Если данные были изменены
                        assignNewValues()
                        changeNote()
                    }
                }
            }
            R.id.action_change_note -> {
                if (checkChangeCurrentValues()) {
                    if (checkEditTexts()) {
                        assignCurrentValues()
                        assignNewValues()
                        clearFocus()
                    } else {
                        Toast.makeText(this, getString(R.string.action_canceled), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_RESTORE_CURRENT_NOTE_COLOR, intermediateVariableNoteColor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        intermediateVariableNoteColor = savedInstanceState.getString(KEY_RESTORE_CURRENT_NOTE_COLOR).toString()
        DrawableCompat.setTint(binding.changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(intermediateVariableNoteColor))
    }

    override fun onClickPositive() {
        assignCurrentValues()
        assignNewValues()
        changeNote()
    }

    override fun onClickNegative() {
        Toast.makeText(this, getString(R.string.action_canceled), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onClick(v: View?) {
        MaterialColorPickerDialog
            .Builder(this)
            .setTitle(getString(R.string.description_choose_color))
            .setColorRes(resources.getIntArray(R.array.noteItemColors).toList())
            .setColorListener { _, colorHex ->
                intermediateVariableNoteColor = colorHex
                DrawableCompat.setTint(binding.changeNoteItemContent.textChangeNoteColor.background, Color.parseColor(intermediateVariableNoteColor))
                itemChangeNote?.isVisible = currentNoteColor != intermediateVariableNoteColor
            }
            .show()
    }

    private val onItemSpinnerClick = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
            itemChangeNote?.isVisible = currentNotePriority != id
        }
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
        itemChangeNote?.isVisible = checkChangeCurrentValues()
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun checkChangeCurrentValues(): Boolean {
        return currentNoteColor != intermediateVariableNoteColor ||
                currentNotePriority != binding.changeNoteItemContent.spinnerNoteNewPriority.selectedItemId ||
                currentNoteTitle != binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString() ||
                currentNoteContent != binding.changeNoteItemContent.editChangeNoteItemContent.text.toString()
    }

    private fun checkEditTexts(): Boolean {
        return binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString().isNotEmpty() ||
                binding.changeNoteItemContent.editChangeNoteItemContent.text.toString().isNotEmpty()
    }

    private fun checkChangeNewValues(): Boolean {
        return newNoteColor != currentNoteColor ||
        newNotePriority != currentNotePriority ||
        newNoteTitle != currentNoteTitle ||
        newNoteContent != currentNoteContent
    }

    private fun assignCurrentValues() {
        currentNoteColor = intermediateVariableNoteColor
        currentNotePriority = binding.changeNoteItemContent.spinnerNoteNewPriority.selectedItemId
        currentNoteTitle = binding.changeNoteItemContent.editChangeNoteItemTitle.text.toString()
        currentNoteContent = binding.changeNoteItemContent.editChangeNoteItemContent.text.toString()
    }

    private fun assignNewValues () {
        newNoteColor = currentNoteColor
        newNotePriority = currentNotePriority
        newNoteTitle = currentNoteTitle
        newNoteContent = currentNoteContent
        itemChangeNote?.isVisible = false
    }

    private fun clearFocus() {
        binding.changeNoteItemContent.editChangeNoteItemTitle.clearFocus()
        binding.changeNoteItemContent.editChangeNoteItemContent.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        binding.changeNoteItemContent.editChangeNoteItemContent.postDelayed({
            val keyboard: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.changeNoteItemContent.editChangeNoteItemContent.windowToken, 0)
        }, 200)
    }

    private fun changeNote() {
        notesItemViewModel.updateNoteItem(
            newNoteTitle!!,
            newNoteContent!!,
            newNoteColor!!,
            newNotePriority!!,
            currentNoteId!!
        )
        Toast.makeText(this, getString(R.string.toast_note_changed), Toast.LENGTH_SHORT).show()
        finish()
    }
}