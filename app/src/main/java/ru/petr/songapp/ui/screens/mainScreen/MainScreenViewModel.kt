package ru.petr.songapp.ui.screens.mainScreen

import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.*
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.data.repositories.SongCollectionsRepository
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionScreenParams
import ru.petr.songapp.ui.screens.songScreens.models.SongScreenParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes

class MainScreenViewModel (repository: SongCollectionsRepository) : ViewModel() {
    private var _allCollections: MutableStateFlow<List<SongCollectionDBModel>?> = MutableStateFlow(null)
    val allCollections = _allCollections.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allCollections.collect {collectionList ->
                _allCollections.value = collectionList
            }
        }
    }

    fun goToSongCollection(songCollectionId: Int) {
        navman.goto(
            composableResId = ComposableResourceIds.SongCollectionsScreen,
            p = SongCollectionScreenParams(
                    modifier = Modifier,
                    songCollectionId = songCollectionId,
            )
        )
    }
}

class MainScreenViewModelFactory(private val repository: SongCollectionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}