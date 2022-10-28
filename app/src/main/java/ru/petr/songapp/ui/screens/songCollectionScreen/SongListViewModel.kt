package ru.petr.songapp.ui.screens.songCollectionScreen

import androidx.compose.ui.Modifier
import androidx.lifecycle.*
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.launch
import ru.petr.songapp.data.models.songData.SongDBModel
import ru.petr.songapp.data.repositories.SongsByCollectionsRepository
import ru.petr.songapp.data.repositories.utils.SongCollectionView
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songScreens.models.SongParams

class SongListViewModel(private val repository: SongsByCollectionsRepository) : ViewModel() {

    val songsByCollections: LiveData<List<SongCollectionView>> = repository.songsByCollections.asLiveData()

    fun insert(songDBModel: SongDBModel) = viewModelScope.launch { repository.insert(songDBModel) }

    fun updateOrGotoSong(id: Int) {
        navman.goto(composableResId = ComposableResourceIds.SongScreen, p = SongParams(Modifier, songId = id))
    }
}

class SongListViewModelFactory(private val repository: SongsByCollectionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}