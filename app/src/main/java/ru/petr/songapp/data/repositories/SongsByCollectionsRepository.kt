package ru.petr.songapp.data.repositories

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import ru.petr.songapp.data.models.songData.SongDBModel
import ru.petr.songapp.data.models.songData.dao.SongDao
import ru.petr.songapp.data.repositories.utils.SongCollectionView

class SongsByCollectionsRepository (private val songDao: SongDao, private val songCollectionsRepository: SongCollectionsRepository) {

    var songsByCollections: Flow<List<SongCollectionView>> =
        songCollectionsRepository.allCollections.map { collections ->
            val songCollectionViewList: MutableList<SongCollectionView> = mutableListOf()
            collections.forEach { collection ->
                songCollectionViewList.add(SongCollectionView(collection, songDao.getCollectionSongsNames(collection.id)))
            }
            songCollectionViewList
        }

    init {
        /*CoroutineScope(Dispatchers.IO).launch {
            *//*songCollectionsRepository.allCollections.collect { collections ->
                songsByCollections = flow{
                    val songCollectionViewList: MutableList<SongCollectionView> = mutableListOf()
                    collections.forEach {
                        songCollectionViewList.add(SongCollectionView(it, songDao.getCollectionSongsNames(it.id)))
                    }
                    emit(songCollectionViewList)
                }
            }*//*

            songsByCollections = songCollectionsRepository.allCollections.map { collections ->
                val songCollectionViewList: MutableList<SongCollectionView> = mutableListOf()
                collections.forEach { collection ->
                    songCollectionViewList.add(SongCollectionView(collection, songDao.getCollectionSongsNames(collection.id)))
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