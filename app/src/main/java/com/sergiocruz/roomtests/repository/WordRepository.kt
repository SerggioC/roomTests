package com.sergiocruz.roomtests.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.sergiocruz.roomtests.database.WordDao
import com.sergiocruz.roomtests.model.User
import com.sergiocruz.roomtests.model.UserAndLibrary
import com.sergiocruz.roomtests.model.UserWithPlaylists
import com.sergiocruz.roomtests.model.Word
import com.sergiocruz.roomtests.model.queries.UserWithAddress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(word: Word): Long {
        return wordDao.insert(word)
    }

    @WorkerThread
    suspend fun deleteWord(word: Word) = wordDao.deleteWord(word)

    @WorkerThread
    suspend fun getUserWithAddress(): UserWithAddress? {
        val usersAndAddresses = wordDao.getUsersAndAddresses()
        return usersAndAddresses
    }

    suspend fun getAllUsers(): List<User>? {
        delay(500)
        return wordDao.getAllUsers()
    }


    @WorkerThread
    suspend fun getUserWithID(id: Int): List<User>? {
        val userWithID = wordDao.getUserWithID(id)
        return userWithID
    }

    @WorkerThread
    suspend fun getUserWithLibrary(): List<UserAndLibrary> {
        val userWithID = wordDao.getUsersAndLibraries()
        return userWithID
    }

    @WorkerThread
    suspend fun getUsersWithPlayLists() = wordDao.getPlayListsForUser()


}
