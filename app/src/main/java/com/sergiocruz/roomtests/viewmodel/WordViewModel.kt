package com.sergiocruz.roomtests.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.sergiocruz.roomtests.model.User
import com.sergiocruz.roomtests.model.UserAndLibrary
import com.sergiocruz.roomtests.model.UserWithPlaylists
import com.sergiocruz.roomtests.model.Word
import com.sergiocruz.roomtests.model.queries.UserWithAddress
import com.sergiocruz.roomtests.repository.WordRepository
import kotlinx.coroutines.*
import java.util.*

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun getUsersAddresses(): LiveData<UserWithAddress?> {
//        val asLiveData = repository.getUserWithAddress()?.asLiveData()
//        return asLiveData
         return liveDataWithLoading {
             delay(500) /* fake loading time */
             repository.getUserWithAddress()
        }
    }

    suspend fun getAllUsers() = repository.getAllUsers()

    /** Force LiveData Update! **/
    fun triggerGetAllUsers() {
        _allUsersTrigger.value = Unit
    }

    private val _allUsersTrigger = MutableLiveData<Unit>()
    val allUsers: LiveData<List<User>?> = _allUsersTrigger.switchMap {
        liveDataWithLoading {
            delay(500) /* fake loading time */
            repository.getAllUsers()
        }
    }


    /**
     * Helper to load suspend block with progressBar.
     * Must have an observer to emit.
     * **/
    private fun <T>liveDataWithLoading(block: suspend () -> T?): LiveData<T?> {
        return liveData {
            _progressBarVisibility.value = View.VISIBLE
            emit(block())
            _progressBarVisibility.value = View.GONE
        }
    }



    var counter = 0

    private suspend fun simulateNetworkDataFetch(): String = withContext(Dispatchers.IO) {
        delay(3000)
        counter++
        return@withContext "New data from request #$counter"
    }

    suspend fun getUserWithID(id: Int): List<User>? {
        val userWithID = repository.getUserWithID(id)
        delay(2000)
        return userWithID
    }



    fun getUserAndLibrary(): LiveData<List<UserAndLibrary>?> {
        return liveDataWithLoading {
            delay(500) // remove fake delay
            repository.getUserWithLibrary().also {
                print(it)
            }
        }
    }

    fun getUsersWithPlayLists(callback: (Any?) -> Unit) {
        viewModelScope.launch {
            val up = repository.getUsersWithPlayLists()
            callback.invoke(up)
        }
    }



    private val data = MutableLiveData<String>()

    val loginService: LiveData<String> = data.switchMap {
        liveData {
            emit("loading...")
            emit(attemptLogin())
        }
    }

    fun loginClick(newData: String) {
        data.value = newData
    }

    /**let pretend this is coming from our repository
     * with the business logic of authenticating
     */
    private suspend fun attemptLogin(): String {
        delay(2000)
        //do business logic here...

        //return fake login response
        return "login successfully"
    }


    /**
     * Request a snackbar to display a string.
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     */
    private val _snackBar = MutableLiveData<String?>()

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String?>
        get() = _snackBar


    private val _progressBarVisibility = MutableLiveData(View.GONE)

    /**
     * Show a loading spinner if true
     */
    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility


    private suspend fun launchDataLoadWithSpinner(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _progressBarVisibility.value = View.VISIBLE
                block()
            } catch (e: Exception) {
                _snackBar.value = e.message
            } finally {
                _progressBarVisibility.value = View.GONE
            }
        }
    }


}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T): (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}