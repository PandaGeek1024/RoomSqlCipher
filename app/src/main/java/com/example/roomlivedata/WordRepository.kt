package com.example.roomlivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.roomlivedata.db.dao.WordDao
import com.example.roomlivedata.db.entity.Word

class WordRepository(private val wordDao: WordDao?) {

    //    // Room executes all queries on a separate thread.
//    // Observed LiveData will notify the observer when the data has changed.
//    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    val allWords: LiveData<List<Word>> = liveData {
        val data = wordDao?.getAlphabetizedWords() ?: emptyList()
        emit(data)
    }

    suspend fun insert(word: Word) {
        wordDao?.insert(word)
    }
}