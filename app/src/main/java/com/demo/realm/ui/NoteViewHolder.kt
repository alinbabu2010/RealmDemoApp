package com.demo.realm.ui

import androidx.recyclerview.widget.RecyclerView
import com.demo.realm.databinding.NotesRecyclerViewItemBinding
import com.demo.realm.models.Notes

class NoteViewHolder(private val binding: NotesRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Notes, onButtonClicks: NoteAdapter.OnButtonClicks?){

        val displayNote = "${note.message}\nAuthor: ${note.author.name} from ${note.author.place}"

        with(binding){

            txtViewMessage.text = displayNote

            btnDelete.setOnClickListener {
                onButtonClicks?.onDeleteClicked(note.id)
            }

            btnUpdate.setOnClickListener {
                onButtonClicks?.onUpdateClicked(note)
            }
        }

    }

}
