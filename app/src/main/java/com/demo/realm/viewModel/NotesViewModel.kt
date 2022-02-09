package com.demo.realm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.realm.db.dao.NotesDao
import com.demo.realm.models.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel : ViewModel() {

    var lastId = 0

    val operationLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val notesList: MutableLiveData<List<Notes>> by lazy {
        MutableLiveData<List<Notes>>()
    }

    private val onDatabaseOperation = object : NotesDao.OnDatabaseOperation {
        override fun onSuccess() {
            operationLiveData.postValue(true)
        }
    }

    private val notesDao = NotesDao(onDatabaseOperation)

    fun insertNote(notes: Notes) {
        viewModelScope.launch {
            notesDao.insertNote(notes)
        }
    }

    fun deleteNotes(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                notesDao.deleteNotes(id)
            }
        }
    }

    fun updateNotes(notes: Notes) {
        viewModelScope.launch {
            notesDao.updateNote(notes)
        }
    }

    fun fetchAllNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            notesList.postValue(notesDao.fetchAllNotes())
        }
    }

}