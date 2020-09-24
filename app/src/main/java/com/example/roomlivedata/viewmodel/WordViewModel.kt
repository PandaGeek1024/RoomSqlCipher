package com.example.roomlivedata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomlivedata.WordRepository
import com.example.roomlivedata.db.WordRoomDatabase
import com.example.roomlivedata.db.entity.Word
import kotlinx.coroutines.*

class WordViewModel(application: Application) : AndroidViewModel(application) {
    val uiScope = CoroutineScope(Dispatchers.Main)
    private val repository: WordRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application).wordDao()

        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)

    }

    fun test() {
        GlobalScope.launch {
            val deferred = async {
                return@async "${Thread.currentThread()} has run."
            }
        }

    }

    suspend fun test1() = coroutineScope {

    }
}