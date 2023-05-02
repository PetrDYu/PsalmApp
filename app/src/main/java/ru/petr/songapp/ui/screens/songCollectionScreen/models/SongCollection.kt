package ru.petr.songapp.ui.screens.songCollectionScreen.models

import ru.petr.songapp.data.models.room.songData.dao.SongDataForCollection
import ru.petr.songapp.ui.screens.songScreens.models.Song

class SongCollection(val isFixed: Boolean, title: String, shortTitle: String) {
    var mSongs: List<SongDataForCollection> = listOf()
        private set

    fun addSong(songData: SongDataForCollection) {
        if (songData.id in (mSongs.map {it.id})) {
            // TODO Добавить какое-то вразумительное обозначение ошибки или не ошибки
            return
        }
        val mutableSongs = mSongs.toMutableList()
        mutableSongs.add(songData)
        mSongs = mutableSongs
    }

    fun addSongsAsLink(songs: List<SongDataForCollection>) {
        mSongs = songs
    }

    fun openSong(id: Int): Song {
        TODO()
    }

    fun numberIsInCollection(number: Int): Boolean {
        return number in (mSongs.map {it.numberInCollection})
    }
}