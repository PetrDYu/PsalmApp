package ru.petr.songapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.room.songData.dao.SongCollectionDao

class SongCollectionsRepository(private val psalmCollectionDao: SongCollectionDao) {

    val allCollections: Flow<List<SongCollectionDBModel>> = psalmCollectionDao.getAllCollections()

    @WorkerThread
    suspend fun insert(psalmCollection: SongCollectionDBModel){
        psalmCollectionDao.insert(psalmCollection)
    }

}