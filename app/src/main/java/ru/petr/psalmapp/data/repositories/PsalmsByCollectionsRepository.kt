package ru.petr.psalmapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmCollectionDao
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmDao
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView

class PsalmsByCollectionsRepository (private val psalmDao: PsalmDao, private val psalmCollectionsRepository: PsalmCollectionsRepository) {

    var psalmsByCollections: Flow<List<PsalmCollectionView>> =
        psalmCollectionsRepository.allCollections.map { collections ->
            val psalmCollectionViewList: MutableList<PsalmCollectionView> = mutableListOf()
            collections.forEach { collection ->
                psalmCollectionViewList.add(PsalmCollectionView(collection, psalmDao.getCollectionPsalmsNames(collection.id)))
            }
            psalmCollectionViewList
        }

    init {
        /*CoroutineScope(Dispatchers.IO).launch {
            *//*psalmCollectionsRepository.allCollections.collect { collections ->
                psalmsByCollections = flow{
                    val psalmCollectionViewList: MutableList<PsalmCollectionView> = mutableListOf()
                    collections.forEach {
                        psalmCollectionViewList.add(PsalmCollectionView(it, psalmDao.getCollectionPsalmsNames(it.id)))
                    }
                    emit(psalmCollectionViewList)
                }
            }*//*

            psalmsByCollections = psalmCollectionsRepository.allCollections.map { collections ->
                val psalmCollectionViewList: MutableList<PsalmCollectionView> = mutableListOf()
                collections.forEach { collection ->
                    psalmCollectionViewList.add(PsalmCollectionView(collection, psalmDao.getCollectionPsalmsNames(collection.id)))
                }
                psalmCollectionViewList
            }

        }*/

    }

    @WorkerThread
    suspend fun insert(psalm: Psalm) {
        psalmDao.insert(psalm)
    }

}