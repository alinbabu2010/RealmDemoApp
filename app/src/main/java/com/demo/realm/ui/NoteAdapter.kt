package com.demo.realm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.demo.realm.databinding.NotesRecyclerViewItemBinding
import com.demo.realm.models.Notes

class NoteAdapter : ListAdapter<Notes, NoteViewHolder>(NoteDiffUtils()) {

    interface OnButtonClicks{
        fun onUpdateClicked(notes: Notes)
        fun onDeleteClicked(id: Int)
    }

    var onButtonClicks : OnButtonClicks? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            NotesRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note,onButtonClicks)
    }
}