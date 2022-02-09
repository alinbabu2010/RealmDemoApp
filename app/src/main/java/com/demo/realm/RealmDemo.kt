package com.demo.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmDemo : Application() {

    private val realmVersion = 1L

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

}