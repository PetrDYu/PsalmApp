package ru.petr.psalmapp.data.models.psalm_data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection

@Dao
interface PsalmCollectionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(collection: PsalmCollection)

    @Query("SELECT * FROM PsalmCollections")
    fun getAllCollections(): Flow<List<PsalmCollection>>

    @Query("SELECT * FROM PsalmCollections WHERE Id = :id")
    fun getById(id: Int): Flow<PsalmCollection>

    @Update
    fun update(collection: PsalmCollection)

    @Delete
    fun delete(collection: PsalmCollection)
}