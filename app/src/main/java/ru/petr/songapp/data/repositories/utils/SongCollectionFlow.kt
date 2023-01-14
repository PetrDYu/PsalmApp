package ru.petr.songapp.data.repositories.utils

import kotlinx.coroutines.flow.Flow
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.room.songData.dao.ShortSong

data class SongCollectionFlow(val songCollection: SongCollectionDBModel,
                              val songs: Flow<List<ShortSong>>)
