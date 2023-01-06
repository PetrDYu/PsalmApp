package ru.petr.songapp.data.models.songData.dao

import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.songData.SongDBModel
import ru.petr.songapp.data.models.songData.SongWithCollectionFromDB

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(songDBModel: SongDBModel)

    @Query("SELECT Id, NumberInCollection, Name FROM Songs WHERE CollectionId = :collectionId ORDER BY NumberInCollection")
    fun getCollectionSongsNames(collectionId: Int): Flow<List<ShortSong>>

    @Query("SELECT * FROM Songs WHERE Id = :id LIMIT 1")
    fun getSongById(id: Int): Flow<SongDBModel>

    @Transaction
    @Query("SELECT * FROM Songs WHERE Id= :id LIMIT 1")
    fun getSongWithCollectionById(id: Int): Flow<SongWithCollectionFromDB>

    @Update
    suspend fun update(songDBModel: SongDBModel)

    @Delete
    suspend fun delete(songDBModel: SongDBModel)
}

data class ShortSong(val Id: Int, val NumberInCollection: Int, val Name: String)