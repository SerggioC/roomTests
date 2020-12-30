package com.sergiocruz.roomtests.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

@Entity(
    foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = [User.keyUserID],
    childColumns = [Library.keyuserOwnerId],
    onDelete = CASCADE)],
    tableName = Library.tableName)
data class Library(

    @SerializedName(keyLibraryID)
    @ColumnInfo(name = keyLibraryID)
    @PrimaryKey(autoGenerate = true)
    val libraryId: Long? = null,

    @SerializedName(keyTitle)
    @ColumnInfo(name = keyTitle)
    val title: String,

    @SerializedName(keyuserOwnerId)
    @ColumnInfo(name = keyuserOwnerId, index = true)
    val userOwnerId: Long,
) {
    companion object {
        const val tableName = "library_table"
        const val keyLibraryID = "libraryId"
        const val keyTitle = "title"
        const val keyuserOwnerId = "userOwnerId"
    }
}

/** ONE TO ONE RELATION **/
data class UserAndLibrary(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = User.keyUserID,
        entityColumn = Library.keyuserOwnerId)
    val library: Library,
)
