package ru.petr.psalmapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmCollectionDao
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmDao
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView

class PsalmsByCollectionsRepository (private val psalmDao: PsalmDao, private val psalmCollectionsRepository: PsalmCollectionsRepository) {

    var psalmCollectionList: Flow<List<PsalmCollection>> = flowOf(listOf())
    var psalmsByCollections: Flow<List<PsalmCollectionView>> = flowOf(listOf())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            psalmCollectionsRepository.allCollections.collect { collections -> psalmCollectionList = flowOf(collections)}
            psalmsByCollections = flow{
                val psalmCollectionViewList: MutableList<PsalmCollectionView> = mutableListOf()
                psalmCollectionList.collect{collections: List<PsalmCollection> ->
                    collections.forEach {
                        psalmCollectionViewList.add(PsalmCollectionView(it, psalmDao.getCollectionPsalmsNames(it.id)))
                    }
                }
                emit(psalmCollectionViewList)
            }
        }

    }

    @WorkerThread
    suspend fun insert(psalm: Psalm) {
        psalmDao.insert(psalm)
    }

}