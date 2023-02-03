package ru.petr.songapp.ui.screens.songScreens.songEditorScreen

import androidx.lifecycle.*
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.petr.songapp.data.repositories.Settings
import ru.petr.songapp.data.repositories.SettingsRepository
import ru.petr.songapp.data.repositories.SongRepository
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongBuilder

class SongEditorViewModel(private val songRepository: SongRepository, private val settingsRepository: SettingsRepository) : ViewModel() {

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

    fun getSongById(id: Int): LiveData<Song> {
        return songRepository.getSongById(id).map {
            SongBuilder.getSong(it, SongCollection(false, "test", "test"))
        }.asLiveData()
    }

    fun saveFontSizeSetting(newSize: Int) {
        settingsRepository.storeSongFontSize(newSize)
    }
}

class SongEditorViewModelFactory(private val songRepository: SongRepository,
                                 private val settingsRepository: SettingsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongEditorViewModel(songRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
