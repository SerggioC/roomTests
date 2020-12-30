package com.sergiocruz.roomtests.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergiocruz.roomtests.model.*
import com.sergiocruz.roomtests.model.queries.UserWithAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM words_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word): Long

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersAndAddresses(): UserWithAddress?

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAddress(address: Address): Long

    //@RewriteQueriesToDropUnusedColumns
    //@Transaction
    @Query("SELECT * FROM user_table WHERE user_id == :theID LIMIT 1")
    suspend fun getUserWithID(theID: Int): List<User>?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLibrary(li: Library): Long

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUsersAndLibraries(): List<UserAndLibrary>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayList(playlist: Playlist): Long

    @Transaction
    @Query("SELECT * FROM user_table")
    suspend fun getUserPlayLists(): List<UserWithPlaylists>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE userCreatorId == 7")
    suspend fun getPlayListsForUser(): List<Playlist>

    @Insert
    suspend fun insertSong(song: Song): Long

    @Insert
    suspend fun insertPlaylistXRefSong(xref: PlaylistSongCrossRef): Long

    @Insert
    suspend fun insertPlaylistXRefSong(xref: List<PlaylistSongCrossRef>): Array<Long>



    @Transaction
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM songs_table")
    suspend fun getSongsWithPlaylists(): List<SongWithPlaylists>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId == :plID")
    suspend fun getPlaylistsWithSongs(plID: Long): List<PlaylistWithSongs>



    @Query("DELETE FROM words_table")
    suspend fun deleteAllWords()

    @Query("DELETE FROM library_table")
    suspend fun deleteAllLibraries()

    @Query("DELETE FROM songs_table")
    suspend fun deleteAllSongs()

    @Query("DELETE FROM playlist_table")
    suspend fun deleteAllPlayLists()

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM address_table")
    suspend fun deleteAllAddresses()

    @Query("DELETE FROM playlistsongcrossref")
    suspend fun deleteAllPlayListSongXRef()

    @Transaction
    suspend fun deleteEverything() {
        deleteAllWords()
        deleteAllPlayListSongXRef()
        deleteAllSongs()
        deleteAllLibraries()
        deleteAllPlayLists()
        deleteAllUsers()
        deleteAllAddresses()
    }

    @Delete
    suspend fun deleteWord(word: Word)

}