package com.sergiocruz.roomtests.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.sergiocruz.roomtests.typeadapter.DateTypeAdapter
import java.time.LocalDate
import kotlin.collections.ArrayList

@Entity(tableName = User.tableName)
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = userIDKey)
    val userId: Long? = null,

    @SerializedName(firstNameKey)
    val firstName: String?,

    @JsonAdapter(DateTypeAdapter::class)
    @SerializedName(birthDateKey)
    @ColumnInfo(name = birthDateKey)
    val birthDate: LocalDate?,

    @SerializedName(addressKey)
    @Embedded // vai criar a tabela com os campos extra do campo @Embedded
    val address: Address?,

    @SerializedName(secretWordKey)
    @Embedded
    val secretWord: Word?,

    //@Ignore // room
    @Expose(serialize = false, deserialize = false) // gson
    @SerializedName("lista")
    @Embedded
    var lista: ArrayList<Int>? = arrayListOf()

) {

    companion object {
        const val tableName = "user_table"
        const val userIDKey = "user_id"
        const val firstNameKey = "first_name"
        const val birthDateKey = "birth_date"
        const val addressKey = "address"
        const val secretWordKey = "secret_word"
    }
}
