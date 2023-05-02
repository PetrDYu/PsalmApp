package ru.petr.songapp.data.models.room.songData.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.room.songData.SongDBModel
import ru.petr.songapp.data.models.room.songData.SongWithCollectionFromDB

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(songDBModel: SongDBModel)

    @Query("SELECT Id, NumberInCollection, Name FROM Songs WHERE CollectionId = :collectionId ORDER BY NumberInCollection")
    fun getCollectionSongsNames(collectionId: Int): Flow<List<SongDataForCollection>>

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

data class SongDataForCollection(
    @ColumnInfo(name = "Id")
    val id: Int,

    @ColumnInfo(name = "NumberInCollection")
    val numberInCollection: Int,

    @ColumnInfo(name = "Name")
    val name: String
    )