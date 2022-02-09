package com.demo.realm.db.dao

import com.demo.realm.db.entites.AuthorEntity
import com.demo.realm.db.entites.NotesEntity
import com.demo.realm.models.Author
import com.demo.realm.models.Notes
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers

class NotesDao(private val onDatabaseOperation: OnDatabaseOperation) {

    interface OnDatabaseOperation {
        fun onSuccess()
    }

    suspend fun insertNote(notes: Notes) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.insert(mapDataToEntity(notes))
            onDatabaseOperation.onSuccess()
        }
    }

    fun deleteNotes(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { transaction ->
            val note = transaction.where(NotesEntity::class.java)
                .equalTo("id", id).findFirst()
            note?.deleteFromRealm()
            onDatabaseOperation.onSuccess()
        }
    }

    suspend fun updateNote(notes: Notes) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            val note = transaction.where(NotesEntity::class.java).equalTo("id", notes.id)
                .findFirst()
            if (note != null) {
                note.id = notes.id
                note.message = notes.message
                val author = transaction.createObject(AuthorEntity::class.java)
                author.name = notes.author.name
                author.place = notes.author.place
                note.author = author
                onDatabaseOperation.onSuccess()
            }

        }
    }


    fun fetchAllNotes(): List<Notes> {
        val realm = Realm.getDefaultInstance()
        val notesList = mutableListOf<Notes>()
        realm.executeTransaction { transaction ->
            notesList.addAll(
                mapEntityListToDataList(
                    transaction.where(NotesEntity::class.java).findAll()
                )
            )
        }
        return notesList
    }


    private fun mapEntityToData(notesEntity: NotesEntity): Notes = Notes(
        id = notesEntity.id,
        message = notesEntity.message,
        author = Author(
            notesEntity.author?.name ?: "",
            notesEntity.author?.place ?: ""
        )
    )

    private fun mapDataToEntity(notes: Notes): NotesEntity = NotesEntity(
        id = notes.id,
        message = notes.message,
        author = AuthorEntity(
            notes.author.name,
            notes.author.place
        )
    )

    private fun mapEntityListToDataList(notesEntityList: List<NotesEntity>): List<Notes> {
        val dataList = mutableListOf<Notes>()
        notesEntityList.forEach {
            dataList.add(mapEntityToData(it))
        }
        return dataList
    }

}