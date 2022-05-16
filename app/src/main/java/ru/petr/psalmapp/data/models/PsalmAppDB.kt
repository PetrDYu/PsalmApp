package ru.petr.psalmapp.data.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.internal.synchronized
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmCollectionDao
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmDao

@Database(entities = [Psalm::class, PsalmCollection::class], version = 1, exportSchema = true)
abstract class PsalmAppDB : RoomDatabase() {
    abstract fun PsalmDao(): PsalmDao
    abstract fun PsalmCollectionDao(): PsalmCollectionDao

    private class PsalmAppDBCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //TODO Здесь надо сделать заполнение БД при первом запуске приложения
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
                    .addCallback(PsalmAppDBCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}