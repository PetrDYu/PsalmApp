package ru.petr.songapp.ui.screens.songCollectionScreen.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.songData.dao.ShortSong

data class SongCollectionView(val songCollection: SongCollectionDBModel,
                              val songs: List<ShortSong>
)
