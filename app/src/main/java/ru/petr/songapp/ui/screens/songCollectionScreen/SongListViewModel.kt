package ru.petr.songapp.ui.screens.songCollectionScreen

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.*
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.songData.SongDBModel
import ru.petr.songapp.data.models.songData.dao.ShortSong
import ru.petr.songapp.data.repositories.SongsByCollectionsRepository
import ru.petr.songapp.data.repositories.utils.SongCollectionFlow
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.screens.songScreens.models.SongParams

private const val LOG_TAG = "SongListViewModel"

class SongListViewModel(private val repository: SongsByCollectionsRepository) : ViewModel() {

    private val _songsByCollections: MutableStateFlow<List<SongCollectionView>?> = MutableStateFlow(null)
    val songsByCollections = _songsByCollections.asStateFlow()
    private var _copySongsByCollection: List<SongCollectionView>? = null

    private val _searchViewActive = MutableStateFlow(false)
    val searchViewActive = _searchViewActive.asStateFlow()

    init {
        viewModelScope.launch {
            val songCollectionViewList = mutableListOf<SongCollectionView>()
            repository.songsByCollections.collect {
                songCollectionViewList.clear()
                it.forEachIndexed { index, songCollectionFlow ->
                    songCollectionViewList.add(SongCollectionView(songCollectionFlow.songCollection, listOf()))
                    launch {
                        songCollectionFlow.songs.collect {  songs ->
                            songCollectionViewList[index] = SongCollectionView(
                                songCollectionFlow.songCollection,
                                songs
                            )
                            _songsByCollections.value = songCollectionViewList
                        }
                    }
                }
            }
        }
    }

    fun insert(songDBModel: SongDBModel) = viewModelScope.launch { repository.insert(songDBModel) }

    fun updateOrGotoSong(id: Int) {
        navman.goto(composableResId = ComposableResourceIds.SongScreen, p = SongParams(Modifier, songId = id))
    }

    fun searchSongs(searchText: String) {
        _copySongsByCollection = _songsByCollections.value?.toList()
        _songsByCollections.value?.toMutableList()?.let { songCollectionViews ->
            songCollectionViews.forEachIndexed { index, songCollectionView ->
                val songCollectionViewNew = SongCollectionView(
                    songCollectionView.songCollection,
                    songCollectionView.songs.filter { searchText.lowercase() in it.Name.lowercase() }
                )
                songCollectionViews[index] = songCollectionViewNew
            }

            _songsByCollections.value = songCollectionViews
            _searchViewActive.value = true
        }
    }

    fun backFromSearch() {
        _songsByCollections.value = _copySongsByCollection
        Log.d(LOG_TAG, "backFromSearch")
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