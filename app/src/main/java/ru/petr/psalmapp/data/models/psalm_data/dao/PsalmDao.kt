package ru.petr.psalmapp.data.models.psalm_data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.petr.psalmapp.data.models.psalm_data.Psalm

@Dao
interface PsalmDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(psalm: Psalm)

    @Query("SELECT Id, NumberInCollection, Name FROM Psalms WHERE CollectionId = :collectionId ORDER BY NumberInCollection")
    fun getCollectionPsalmsNames(collectionId: Int): Flow<List<ShortPsalm>>

    @Query("SELECT * FROM Psalms WHERE Id = :id LIMIT 1")
    fun getPsalmById(id: Int): Flow<Psalm>

    @Update
    suspend fun update(psalm: Psalm)

    @Delete
    suspend fun delete(psalm: Psalm)
}

data class ShortPsalm(val Id: Int, val NumberInCollection: Int, val Name: String)