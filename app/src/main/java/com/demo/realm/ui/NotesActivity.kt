package com.demo.realm.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.realm.R
import com.demo.realm.databinding.ActivityNotesBinding
import com.demo.realm.databinding.NoteDialogBinding
import com.demo.realm.models.Author
import com.demo.realm.models.Notes
import com.demo.realm.viewModel.NotesViewModel

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    lateinit var adapter: NoteAdapter
    val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupListeners()
        observeLiveData()
        viewModel.fetchAllNotes()
    }

    private fun setupListeners() {

        adapter.onButtonClicks = object : NoteAdapter.OnButtonClicks {

            override fun onUpdateClicked(notes: Notes) {
                showInsertDialog(true, notes)
            }

            override fun onDeleteClicked(id: Int) {
                viewModel.deleteNotes(id)
            }

        }

        binding.btnAdd.setOnClickListener {
            showInsertDialog()
        }

    }

    private fun setupAdapter(){
        adapter = NoteAdapter()
        with(binding.rvMain){
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = this@NotesActivity.adapter
        }
    }


    private fun showInsertDialog(isUpdate: Boolean = false, notes: Notes? = null) {
        val dialogViewBinding = NoteDialogBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle(if (isUpdate) R.string.update_notes else R.string.add_notes)
            setView(dialogViewBinding.root)
            if (isUpdate) notes?.let { dialogViewBinding.populateForm(it) }
            setPositiveButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            setNegativeButton(if (isUpdate) R.string.update else R.string.add) { dialog, _ ->
                val id = notes?.id ?: (viewModel.lastId + 1)
                setNegativeButtonAction(isUpdate, dialogViewBinding, id)
                dialog.cancel()
            }
        }.create().show()
    }

    private fun NoteDialogBinding.populateForm(notes: Notes) {
        nameInputEditText.setText(notes.author.name)
        placeInputEditText.setText(notes.author.place)
        noteInputEditText.setText(notes.message)
    }

    private fun NoteDialogBinding.getFormData(id: Int): Notes {
        val author = Author(
            nameInputEditText.text.toString(),
            placeInputEditText.text.toString()
        )
        return Notes(
            id,
            noteInputEditText.text.toString(),
            author
        )
    }

    private fun setNegativeButtonAction(
        isUpdate: Boolean,
        dialogViewBinding: NoteDialogBinding,
        id: Int
    ) {
        val notes = dialogViewBinding.getFormData(id)
        if (isUpdate) {
            viewModel.updateNotes(notes)
        } else {
            viewModel.insertNote(notes)
        }
    }

    private fun observeLiveData() {

        viewModel.operationLiveData.observe(this) {
            if (it) viewModel.fetchAllNotes()
        }

        viewModel.notesList.observe(this) {
            viewModel.lastId = if (it.isNotEmpty()) it.last().id else 0
            adapter.submitList(it)
        }

    }

}