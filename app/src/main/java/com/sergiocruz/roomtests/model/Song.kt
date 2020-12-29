package com.sergiocruz.roomtests.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = Song.tableName)
data class Song(
    @SerializedName(keySongID)
    @ColumnInfo(name = keySongID)
    @PrimaryKey(autoGenerate = true)
    val songId: Long? = null,

    @SerializedName(keySongName)
    @ColumnInfo(name = keySongName)
    val songName: String,

    @SerializedName(keyArtist)
    @ColumnInfo(name = keyArtist)
    val artist: String,
) {
    companion object {
        const val tableName = "songs_table"
        const val keySongID = "songId"
        const val keySongName = "song_name"
        const val keyArtist = "artist"
    }
}

/**
 *  MANY TO MANY RELATIONSHIP
 *  **/
@Entity(
    primaryKeys = [PlaylistSongCrossRef.keyPlaylistID, PlaylistSongCrossRef.keySongId],
    foreignKeys = [ // enforce ao adicionar novo item
        ForeignKey(entity = Playlist::class,
            parentColumns = [Playlist.keyPlaylistID],
            childColumns = [PlaylistSongCrossRef.keyPlaylistID]),
        ForeignKey(entity = Song::class,
            parentColumns = [Song.keySongID],
            childColumns = [PlaylistSongCrossRef.keySongId])
    ])
data class PlaylistSongCrossRef(

    @SerializedName(keyPlaylistID)
    @ColumnInfo(name = keyPlaylistID)
    val playlistId: Long,

    @SerializedName(keySongId)
    @ColumnInfo(name = keySongId)
    val songId: Long,
) {
    companion object {
        const val keyPlaylistID = "playlistId"
        const val keySongId = "songId"
    }
}

/**
 * Query data classes
 * */
data class PlaylistWithSongs(

    @Embedded
    val playlist: Playlist,

    @Relation(
        parentColumn = Playlist.keyPlaylistID,
        entityColumn = Song.keySongID,
        associateBy = Junction(PlaylistSongCrossRef::class))
    val songs: List<Song>
)

data class SongWithPlaylists(

    @Embedded
    val song: Song,

    @Relation(
        parentColumn = Song.keySongID,
        entityColumn = Playlist.keyPlaylistID,
        associateBy = Junction(PlaylistSongCrossRef::class))
    val playlists: List<Playlist>
)