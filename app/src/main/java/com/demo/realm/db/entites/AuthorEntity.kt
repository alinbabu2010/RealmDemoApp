package com.demo.realm.db.entites

import io.realm.RealmObject

open class AuthorEntity(
    var name: String = "",
    var place: String = "",
) : RealmObject()