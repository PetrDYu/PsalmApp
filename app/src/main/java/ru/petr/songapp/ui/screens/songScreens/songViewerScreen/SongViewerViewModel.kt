package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.petr.songapp.data.repositories.Settings
import ru.petr.songapp.data.repositories.SettingsRepository
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongBuilder

class SongViewerViewModel(private val songRepository: SongRepository, private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _fontSize: MutableStateFlow<Int> = MutableStateFlow(settingsRepository.settingsMap[Settings.SONG_FONT_SIZE] as Int)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.songFontSize.collect{ newSize -> _fontSize.value = newSize }
        }
    }

    fun getSongById(id: Int): LiveData<Song> {
        return songRepository.getSongById(id).map {
            SongBuilder.getSong(it, SongCollection(false, "test", "test"))
        }.asLiveData()
    }

    fun saveFontSizeSetting(newSize: Int) {
        settingsRepository.storeSongFontSize(newSize)
    }
}

class SongViewerViewModelFactory(private val songRepository: SongRepository,
                                 private val settingsRepository: SettingsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewerViewModel(songRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
