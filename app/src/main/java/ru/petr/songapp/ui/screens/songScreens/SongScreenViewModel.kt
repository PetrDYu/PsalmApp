package ru.petr.songapp.ui.screens.songScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.petr.songapp.data.repositories.Settings
import ru.petr.songapp.data.repositories.SettingsRepository
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongBuilder
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewerViewModel

class SongScreenViewModel(private val songRepository: SongRepository, private val settingsRepository: SettingsRepository) : ViewModel() {
    // Initialize settings with default values
    private val _fontSize: MutableStateFlow<Int> = MutableStateFlow(settingsRepository.settingsMap[Settings.SONG_FONT_SIZE] as Int)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()

    private val _proModeIsActive: MutableStateFlow<Boolean> = MutableStateFlow(settingsRepository.settingsMap[Settings.PRO_MODE_IS_ACTIVE] as Boolean)
    val proModeIsActive: StateFlow<Boolean> = _proModeIsActive.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.songFontSize.collect{ newSize -> _fontSize.value = newSize }
        }
        viewModelScope.launch {
            settingsRepository.proModeIsActive.collect{ newValue -> _proModeIsActive.value = newValue }
        }
    }

    fun getSongById(id: Int, songCollection: SongCollection): LiveData<Song> {
        return songRepository.getSongById(id).map {
            SongBuilder.getSong(it, songCollection)
        }.asLiveData()
    }

    fun saveFontSizeSetting(newSize: Int) {
        settingsRepository.storeSongFontSize(newSize)
    }
}

class SongScreenViewModelFactory(private val songRepository: SongRepository,
                                 private val settingsRepository: SettingsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongScreenViewModel(songRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}