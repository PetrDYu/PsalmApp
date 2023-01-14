package ru.petr.songapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import ru.petr.songapp.data.models.room.songData.SongDBModel
import ru.petr.songapp.data.models.room.songData.dao.SongDao
import ru.petr.songapp.data.repositories.utils.SongCollectionFlow

class SongsByCollectionsRepository (private val songDao: SongDao, private val songCollectionsRepository: SongCollectionsRepository) {

    var songsByCollections: Flow<List<SongCollectionFlow>> =
        songCollectionsRepository.allCollections.map { collections ->
            val songCollectionFlowList: MutableList<SongCollectionFlow> = mutableListOf()
            collections.forEach { collection ->
                songCollectionFlowList.add(SongCollectionFlow(collection, songDao.getCollectionSongsNames(collection.id)))
            }
            songCollectionFlowList
        }

    init {
        /*CoroutineScope(Dispatchers.IO).launch {
            *//*songCollectionsRepository.allCollections.collect { collections ->
                songsByCollections = flow{
                    val songCollectionViewList: MutableList<SongCollectionFlow> = mutableListOf()
                    collections.forEach {
                        songCollectionViewList.add(SongCollectionFlow(it, songDao.getCollectionSongsNames(it.id)))
                    }
                    emit(songCollectionViewList)
                }
            }*//*

            songsByCollections = songCollectionsRepository.allCollections.map { collections ->
                val songCollectionViewList: MutableList<SongCollectionFlow> = mutableListOf()
                collections.forEach { collection ->
                    songCollectionViewList.add(SongCollectionFlow(collection, songDao.getCollectionSongsNames(collection.id)))
                }
                songCollectionViewList
            }

        }*/

    }

    @WorkerThread
    suspend fun insert(songDBModel: SongDBModel) {
        songDao.insert(songDBModel)
    }

}