package ru.petr.psalmapp.data.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import ru.petr.psalmapp.data.models.psalm_data.CollectionSection
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.CollectionSectionDao
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmCollectionDao
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmDao
import ru.petr.psalmapp.data.models.psalm_data.utils.*
import ru.petr.psalmapp.psalmCollections

@Database(entities = [Psalm::class, PsalmCollection::class, CollectionSection::class], version = 1, exportSchema = true)
abstract class PsalmAppDB() : RoomDatabase() {
    abstract fun PsalmDao(): PsalmDao
    abstract fun PsalmCollectionDao(): PsalmCollectionDao
    abstract fun CollectionSectionDao(): CollectionSectionDao

    private class PsalmAppDBCallback(
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
        private var INSTANCE: PsalmAppDB? = null

        fun getDB(context: Context, scope: CoroutineScope): PsalmAppDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PsalmAppDB::class.java,
                    "PsalmAppDB"
                )
                    .addCallback(PsalmAppDBCallback(scope, appContext = context))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}