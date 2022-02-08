package com.demo.realm.db.entites

import io.realm.RealmObject

open class NotesEntity(
    var id: Int = 0,
    var message: String = "",
    var author: AuthorEntity? = null
) : RealmObject()