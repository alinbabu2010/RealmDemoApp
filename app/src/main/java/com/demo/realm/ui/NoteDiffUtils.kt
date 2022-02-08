package com.demo.realm.ui

import androidx.recyclerview.widget.DiffUtil
import com.demo.realm.models.Notes

class NoteDiffUtils : DiffUtil.ItemCallback<Notes>() {

    override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean =
        oldItem == newItem
}