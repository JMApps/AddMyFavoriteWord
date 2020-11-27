package jmapps.addmyfavoriteword.presentation.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentNotesBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter
import jmapps.addmyfavoriteword.presentation.ui.activities.AddNoteActivity
import jmapps.addmyfavoriteword.presentation.ui.adapters.NoteItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.models.NotesItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.AlertUtil
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class NotesFragment : Fragment(), ContractInterface.OtherView, View.OnClickListener,
    AlertUtil.OnClickDelete, SearchView.OnQueryTextListener, NoteItemsAdapter.OnItemClickNote,
    NoteItemsAdapter.OnLongClickNoteItem {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesItemViewModel: NotesItemViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    private lateinit var searchView: SearchView
    private lateinit var noteItemsAdapter: NoteItemsAdapter

    private var defaultOrderIndex = 0
    private lateinit var alertDialog: AlertUtil

    companion object {
        private const val KEY_ORDER_NOTE_ITEM_INDEX = "key_order_note_item_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesItemViewModel = ViewModelProvider(this).get(NotesItemViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        retainInstance = true
        setHasOptionsMenu(true)

        alertDialog = AlertUtil(requireContext(), this)

        defaultOrderIndex = sharedLocalPreferences.getIntValue(KEY_ORDER_NOTE_ITEM_INDEX, 0)!!

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.initView(defaultOrderIndex)

        binding.rvNoteItems.addOnScrollListener(onAddScroll)
        binding.fabAddNote.setOnClickListener(this)

        return binding.root
    }

    override fun initView(orderBy: String) {
        notesItemViewModel.allNoteItems(orderBy).observe(this, {
            it.let {
                val gridLayout = GridLayoutManager(requireContext(), 3)
                binding.rvNoteItems.layoutManager = gridLayout
                noteItemsAdapter = NoteItemsAdapter(requireContext(), it, this, this)
                binding.rvNoteItems.adapter = noteItemsAdapter
                otherFragmentsPresenter.updateState(it)
                otherFragmentsPresenter.defaultState()
            }
        })
    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddNote.startAnimation(show)

        binding.rvNoteItems.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainNoteContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvNoteItems.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainNoteContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_items, menu)
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_notes).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_order_by_add_time -> {
                changeOrderList(defaultOrderIndex = 0)
            }
            R.id.item_order_by_change_time -> {
                changeOrderList(defaultOrderIndex = 1)
            }
            R.id.item_order_by_color -> {
                changeOrderList(defaultOrderIndex = 3)
            }
            R.id.item_order_by_alphabet -> {
                changeOrderList(defaultOrderIndex = 4)
            }
            R.id.item_order_by_priority -> {
                changeOrderList(defaultOrderIndex = 5)
            }
            R.id.action_delete_all_note_items -> {
                alertDialog.showAlertDialog(
                    getString(R.string.dialog_message_are_sure_you_want_item_notes), 0, 0, getString(R.string.action_notes_deleted)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            noteItemsAdapter.filter.filter("")
        } else {
            noteItemsAdapter.filter.filter(newText)
        }
        return true
    }

    override fun onClick(v: View?) {
        toAddNoteActivity()
    }

    override fun onItemClickNote(noteId: Long, noteTitle: String, noteContent: String, noteColor: String, notePriority: Long) {
        Toast.makeText(requireContext(), "Открыть заметку", Toast.LENGTH_SHORT).show()
    }

    override fun itemClickRenameNote(noteId: Long, noteTitle: String, noteContent: String, noteColor: String, notePriority: Long) {
        Toast.makeText(requireContext(), "Изменить заметку", Toast.LENGTH_SHORT).show()
    }

    override fun itemClickDeleteNote(noteId: Long) {
        alertDialog.showAlertDialog(
            getString(R.string.dialog_message_are_sure_you_want_item_note), 1, noteId, getString(R.string.action_note_deleted)
        )
    }

    override fun onClickDeleteOnly(_id: Long) {
        notesItemViewModel.deleteNoteItem(_id)
    }

    override fun onClickDeleteAll() {
        notesItemViewModel.deleteAllNoteItems()
    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.fabAddNote.hide()
            } else {
                binding.fabAddNote.show()
            }
        }
    }

    private fun changeOrderList(defaultOrderIndex: Int) {
        otherFragmentsPresenter.initView(defaultOrderIndex)
        sharedLocalPreferences.saveIntValue(KEY_ORDER_NOTE_ITEM_INDEX, defaultOrderIndex)
    }

    private fun toAddNoteActivity() {
        val toNoteActivity = Intent(requireContext(), AddNoteActivity::class.java)
        startActivity(toNoteActivity)
    }
}