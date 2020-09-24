package com.example.roomlivedata.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")

class Word(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,

    @ColumnInfo(name = "word") val word: String

)