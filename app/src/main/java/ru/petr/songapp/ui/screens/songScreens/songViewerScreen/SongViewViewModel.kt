package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import ru.petr.songapp.data.models.songData.SongWithCollectionFromDB
import ru.petr.songapp.data.repositories.SongRepository

class SongViewViewModel(private val songRepository: SongRepository) : ViewModel() {
    fun getSongById(id: Int): LiveData<SongWithCollectionFromDB> {
        return songRepository.getSongById(id).asLiveData()
    }
}

class SongViewViewModelFactory(private val repository: SongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
