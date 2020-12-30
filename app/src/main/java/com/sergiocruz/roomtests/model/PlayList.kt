package com.sergiocruz.roomtests.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

@Entity(
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = [User.keyUserID],
        childColumns = [Playlist.keyUserCreatorID],
        onDelete = CASCADE,
        onUpdate = CASCADE)],
    tableName = Playlist.tableName)
data class Playlist(

    @SerializedName(keyPlaylistID)
    @ColumnInfo(name = keyPlaylistID)
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long? = null,

    @SerializedName(keyUserCreatorID)
    @ColumnInfo(name = keyUserCreatorID, index = true)
    val userCreatorId: Long,

    @SerializedName(keyPlaylistName)
    @ColumnInfo(name = keyPlaylistName)
    val playlistName: String,
) {
    companion object {
        const val tableName = "playlist_table"
        const val keyPlaylistID = "playlistId"
        const val keyUserCreatorID = "userCreatorId"
        const val keyPlaylistName = "playlistName"
    }
}

/** ONE TO MANY RELATION **/
data class UserWithPlaylists(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = User.keyUserID,
        entityColumn = Playlist.keyUserCreatorID)
    val playlists: List<Playlist>,
)