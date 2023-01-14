package ru.petr.songapp.ui.screens.songCollectionScreen.models

import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.room.songData.dao.ShortSong

data class SongCollectionView(val songCollection: SongCollectionDBModel,
                              val songs: List<ShortSong>
)
