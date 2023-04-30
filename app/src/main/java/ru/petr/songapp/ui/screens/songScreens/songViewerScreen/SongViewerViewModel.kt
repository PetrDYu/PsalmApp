package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

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
import ru.petr.songapp.ui.screens.songScreens.models.SongScreenParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongBuilder

class SongViewerViewModel() : ViewModel() {

    fun editSong(id: Int) {
        navman.goto(
            composableResId = ComposableResourceIds.SongScreen,
            p = SongScreenParams(
                songId = id,
                showType = SongShowTypes.EDIT
            )
        )
    }
}

class SongViewerViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
