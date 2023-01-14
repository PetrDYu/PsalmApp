package ru.petr.songapp.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.room.songData.SongWithCollectionFromDB
import ru.petr.songapp.data.models.room.songData.dao.SongDao

class SongRepository(private val psalmDao: SongDao) {

    fun getSongById(id: Int): Flow<SongWithCollectionFromDB> {
        // TODO посмотреть какую коллекцию подцепляет Room к псалму
        return psalmDao.getSongWithCollectionById(id)
    }
}