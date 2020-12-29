package com.sergiocruz.roomtests

import android.app.Application
import com.sergiocruz.roomtests.database.WordRoomDatabase
import com.sergiocruz.roomtests.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getInstance(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }

}