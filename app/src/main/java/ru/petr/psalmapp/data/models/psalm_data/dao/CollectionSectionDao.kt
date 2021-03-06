package ru.petr.psalmapp.data.models.psalm_data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.petr.psalmapp.data.models.psalm_data.CollectionSection

@Dao
interface CollectionSectionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(section: CollectionSection): Long

    @Query("SELECT * FROM CollectionSections WHERE Id = :id LIMIT 1")
    fun getById(id: Int): Flow<CollectionSection>

    @Query("SELECT * FROM CollectionSections WHERE CollectionId = :id ORDER BY NumberInCollection")
    fun getByCollectionId(id: Int): Flow<List<CollectionSection>>

    @Update
    suspend fun update(section: CollectionSection)

    @Delete
    suspend fun delete(section: CollectionSection)
}