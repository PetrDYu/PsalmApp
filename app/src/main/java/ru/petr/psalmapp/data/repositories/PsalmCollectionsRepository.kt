package ru.petr.psalmapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmCollectionDao

class PsalmCollectionsRepository(private val psalmCollectionDao: PsalmCollectionDao) {

    val allCollections: Flow<List<PsalmCollection>> = psalmCollectionDao.getAllCollections()

    @WorkerThread
    suspend fun insert(psalmCollection: PsalmCollection){
        psalmCollectionDao.insert(psalmCollection)
    }

}