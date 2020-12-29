package com.sergiocruz.roomtests.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = Word.tableName)
class Word(
    @SerializedName("wordID")
    @ColumnInfo(name = "wordID")
    @PrimaryKey(autoGenerate = true)
    var wordID: Long? = null,

    @ColumnInfo(name = "word")
    val word: String
) {
    companion object {
        const val tableName = "words_table"
    }
}