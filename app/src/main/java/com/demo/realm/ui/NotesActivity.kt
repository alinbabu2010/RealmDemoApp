package com.demo.realm.ui

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.realm.R
import com.demo.realm.databinding.ActivityNotesBinding
import com.demo.realm.databinding.NoteDialogBinding
import com.demo.realm.models.Author
import com.demo.realm.models.Notes

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
        setupListeners()
    }

    private fun setupListeners() {

        adapter.onButtonClicks = object : NoteAdapter.OnButtonClicks{

            override fun onUpdateClicked() {
                showInsertDialog(true)
            }

            override fun onDeleteClicked() {
                Toast.makeText(this@NotesActivity,"Delete Clicked",Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnAdd.setOnClickListener {
            showInsertDialog()
        }

        val notesList = mutableListOf<Notes>()
        for ( i in 1..10){
            notesList.add(Notes(i,"Sample Note", Author("Jack","San Fransisco")))
        }
        adapter.submitList(notesList)

    }

    private fun setupAdapter(){
        adapter = NoteAdapter()
        with(binding.rvMain){
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = this@NotesActivity.adapter
        }
    }


    private fun showInsertDialog(isUpdate : Boolean = false){
        val dialogViewBinding = NoteDialogBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply{
            setTitle(if (isUpdate) R.string.update_notes else R.string.add_notes)
            setView(dialogViewBinding.root)
            setPositiveButton(R.string.cancel){ dialog, _ ->
                dialog.cancel()
            }
            setNegativeButton(if (isUpdate) R.string.update else R.string.add){ dialog, _ ->
                setNegativeButtonAction(isUpdate, dialogViewBinding)
                dialog.cancel()
            }
        }.create().show()
    }

    private fun setNegativeButtonAction(
        isUpdate: Boolean,
        dialogViewBinding: NoteDialogBinding
    ) {
        val notes: Notes
        if (!isUpdate) {
            with(dialogViewBinding) {
                val author = Author(
                    nameInputEditText.text.toString(),
                    placeInputEditText.text.toString()
                )
                notes = Notes(
                    1,
                    noteInputEditText.text.toString(),
                    author
                )
            }
            Toast.makeText(this@NotesActivity, "$notes", Toast.LENGTH_SHORT).show()
        }
    }


}