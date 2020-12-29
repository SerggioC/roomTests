package com.sergiocruz.roomtests.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.sergiocruz.roomtests.typeadapter.DateConverter
import com.sergiocruz.roomtests.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.math.log

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [User::class, Address::class, Word::class, Library::class, Playlist::class, Song::class, PlaylistSongCrossRef::class],
    version = 3,
    exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {

        private const val DATABASE_NAME = "words_database"

        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            return Room.databaseBuilder(context, WordRoomDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(WordDatabaseCallback(scope))
                .build()
        }


        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database: WordRoomDatabase ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }

            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
//                INSTANCE?.let { database: WordRoomDatabase ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.wordDao())
//                    }
//                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(wordDao: WordDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            wordDao.deleteEverything()

            var word = Word(word = "Hello")
            wordDao.insert(word)
            word = Word(word = "World!")
            wordDao.insert(word)
            word = Word(word = "secret word!")
            val wordID = wordDao.insert(word)
            word.wordID = wordID

            val address = Address(addressID = null,
                city = "Heree",
                postCode = 234560,
                street = "Street das flores")
            val id = wordDao.insertAddress(address)
            address.addressID = id

            val user = User(firstName = "JACK!", address = address, birthDate = LocalDate.now(), secretWord = word)
            val uid = wordDao.insertUser(user)

            val newLib = Library(title = "Minha bilbioteca", userOwnerId = uid)
            wordDao.insertLibrary(newLib)

            val user2 = User(
                firstName = "JACKY CHAN!",
                address = address,
                birthDate = LocalDate.now(),
                secretWord = word)
            val uid2 = wordDao.insertUser(user2)

            val newLib2 = Library(title = "segunda bilbioteca do JACKY CHAN!", userOwnerId = uid2)
            wordDao.insertLibrary(newLib2)


            var pl = Playlist(userCreatorId = uid, playlistName = "ROCK MUSIC!")
            val plid1 = wordDao.insertPlayList(pl)

            pl = Playlist(userCreatorId = uid, playlistName = "MUSICA PAH!")
            val plid2 = wordDao.insertPlayList(pl)

            pl = Playlist(userCreatorId = uid2, playlistName = "MUSICA PIRILAU! HEHEHEHE 😀 😀 😀 do userID: $uid2")
            val plid3 = wordDao.insertPlayList(pl)


            // insert songs
            val song = Song(songName = "songa um", artist = "Bruce")
            val songid1 = wordDao.insertSong(song)

            val song2 = Song(songName = "musica dois", artist = "Lee")
            val songid2 = wordDao.insertSong(song2)

            val song3 = Song(songName = "triple song", artist = "Springsteen")
            val songid3 = wordDao.insertSong(song3)

            val song4 = Song(songName = "song of four", artist = "Forty")
            val songid4 = wordDao.insertSong(song4)

            val song5 = Song(songName = "penta five", artist = "Chico")
            val songid5 = wordDao.insertSong(song5)

            val song6 = Song(songName = "hexadecimal sixth song", artist = "HEX!")
            val songid6 = wordDao.insertSong(song6)


            //define crossref song/playlist
            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid1, songId = songid1))
            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid1, songId = songid4))
            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid1, songId = songid5))

            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid2, songId = songid2))
            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid2, songId = songid3))
            wordDao.insertPlaylistXRefSong(PlaylistSongCrossRef(playlistId = plid2, songId = songid6))

            val lista = listOf(
                PlaylistSongCrossRef(playlistId = plid3, songId = songid1),
                PlaylistSongCrossRef(playlistId = plid3, songId = songid2),
                PlaylistSongCrossRef(playlistId = plid3, songId = songid3),
                PlaylistSongCrossRef(playlistId = plid3, songId = songid4),
                PlaylistSongCrossRef(playlistId = plid3, songId = songid5),
                PlaylistSongCrossRef(playlistId = plid3, songId = songid6),
            )
            wordDao.insertPlaylistXRefSong(lista)

            val q = wordDao.getPlaylistsWithSongs()
            val w = wordDao.getSongsWithPlaylists()
            val e = wordDao.getPlaylistsWithSongs(plid3)
            Log.i("Sergio", "populateDatabase: $w")

        }
    }
}