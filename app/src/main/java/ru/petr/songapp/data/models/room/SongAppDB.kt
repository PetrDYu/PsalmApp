package ru.petr.songapp.data.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.petr.songapp.data.models.room.songData.CollectionSection
import ru.petr.songapp.data.models.room.songData.SongDBModel
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.room.songData.dao.CollectionSectionDao
import ru.petr.songapp.data.models.room.songData.dao.SongCollectionDao
import ru.petr.songapp.data.models.room.songData.dao.SongDao
import ru.petr.songapp.data.models.room.songData.utils.populateDBFromAssets

//import ru.petr.songapp.songCollections

@Database(entities = [SongDBModel::class, SongCollectionDBModel::class, CollectionSection::class], version = 1, exportSchema = true)
abstract class SongAppDB() : RoomDatabase() {
    abstract fun SongDao(): SongDao
    abstract fun SongCollectionDao(): SongCollectionDao
    abstract fun CollectionSectionDao(): CollectionSectionDao

    private class SongAppDBCallback(
        private val scope: CoroutineScope,
        val appContext: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDBFromAssets(appContext, database)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SongAppDB? = null

        fun getDB(context: Context, scope: CoroutineScope): SongAppDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongAppDB::class.java,
                    "SongAppDB"
                )
                    .addCallback(SongAppDBCallback(scope, appContext = context))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}