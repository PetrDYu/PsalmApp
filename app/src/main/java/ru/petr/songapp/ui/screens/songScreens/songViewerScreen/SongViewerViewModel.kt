package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongBuilder

class SongViewerViewModel(private val songRepository: SongRepository) : ViewModel() {

    fun getSongById(id: Int): LiveData<Song> {
        return songRepository.getSongById(id).map {
            SongBuilder.getSong(it, SongCollection(false, "test", "test"))
        }.asLiveData()
    }
}

class SongViewerViewModelFactory(private val repository: SongRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
