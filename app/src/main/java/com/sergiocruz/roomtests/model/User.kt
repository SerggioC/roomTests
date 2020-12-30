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
    @ColumnInfo(name = keyUserID)
    val userId: Long? = null,

    @SerializedName(keyFirstName)
    @ColumnInfo(name = keyFirstName)
    val firstName: String?,

    @SerializedName(keyLastName)
    @ColumnInfo(name = keyLastName)
    val lastName: String? = "",

    @JsonAdapter(DateTypeAdapter::class)
    @SerializedName(keyBirthDate)
    @ColumnInfo(name = keyBirthDate)
    val birthDate: LocalDate?,

    @SerializedName(keyAddress)
    @Embedded // vai criar a tabela com os campos extra do campo @Embedded
    val address: Address?,

    @SerializedName(keySecretWord)
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
        const val keyUserID = "user_id"
        const val keyFirstName = "first_name"
        const val keyLastName = "last_name"
        const val keyBirthDate = "birth_date"
        const val keyAddress = "address"
        const val keySecretWord = "secret_word"
    }
}
