package com.sergiocruz.roomtests

import android.app.Application
import android.database.Cursor
import android.os.StrictMode
import androidx.room.Database
import com.sergiocruz.roomtests.database.WordRoomDatabase
import com.sergiocruz.roomtests.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.*

class App : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getInstance(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        val threadPolicy = StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork() // or .detectAll() for all detectable problems
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(threadPolicy)
        val policy = StrictMode.VmPolicy.Builder()
            .detectLeakedClosableObjects()
            .detectLeakedSqlLiteObjects()
            .penaltyDeath()
            .penaltyLog()
            .build()
        StrictMode.setVmPolicy(policy)
        super.onCreate()
    }


    /**
     * - Queries mais pequenas com limite de linhas retornadas e offset (LIMIT 10 p.ex.) (LIMIT row_count OFFSET offset)
     * - Query só aos campos estritamente necessários
     * - Tabelas com menos colunas para não encher a window tão rapidamente
     * - Adicionar indexes para evitar full table searches e encher a memória
     * - Room @RewriteQueriesToDropUnusedColumns nas queries do DAO
     * - Alterar cursor window
     * if (cursor is SQLiteCursor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    (cursor as SQLiteCursor).window = CursorWindow(null, 1024 * 1024 * 10)
    }
     * */
    fun FIX_OUT_OF_MEMORY_SQLITE() {
        val list = mutableListOf<Product>()
        var offset = 0
        var cursorHasData = true
        while (cursorHasData) {
            val statement = "SELECT * FROM Table ORDER someField LIMIT 10 OFFSET $offset"
            val cursor: Cursor = database.query(statement, null)
            while (cursor.moveToNext()) {
                val product = Product()
                product.setAllValuesFromCursor(cursor)
                list.add(product)
            }
            cursor.close()
            cursorHasData = cursor.count > 0
            offset += 10
        }
    }

}