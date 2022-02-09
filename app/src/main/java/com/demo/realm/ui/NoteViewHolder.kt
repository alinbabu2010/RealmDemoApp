package com.demo.realm.ui

import androidx.recyclerview.widget.RecyclerView
import com.demo.realm.databinding.NotesRecyclerViewItemBinding
import com.demo.realm.models.Notes

class NoteViewHolder(private val binding: NotesRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Notes, onButtonClicks: NoteAdapter.OnButtonClicks?){

        val author = "Author: ${note.author.name} -- ${note.author.place}"

        with(binding) {

            txtViewMessage.text = note.message
            txtViewAuthor.text = author

            btnDelete.setOnClickListener {
                onButtonClicks?.onDeleteClicked(note.id)
            }

            btnUpdate.setOnClickListener {
                onButtonClicks?.onUpdateClicked(note)
            }
        }

    }

}
