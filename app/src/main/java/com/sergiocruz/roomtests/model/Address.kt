package com.sergiocruz.roomtests.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = Address.tableName)
data class Address(

    @SerializedName(addressIdKey)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = addressIdKey)
    var addressID: Long?,

    @SerializedName(cityKey)
    @ColumnInfo(name = cityKey)
    val city: String?,

    @SerializedName(streetKey)
    @ColumnInfo(name = streetKey)
    val street: String?,

    @SerializedName(postCodeKey)
    @ColumnInfo(name = postCodeKey)
    val postCode: Int
) {
    companion object {
        const val tableName = "address_table"
        const val addressIdKey = "addressID"
        const val cityKey = "city"
        const val streetKey = "street"
        const val postCodeKey = "post_code"
    }
}
