package ru.petr.songapp.ui.screens.songCollectionScreen

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.petr.songapp.data.models.room.songData.SongDBModel
import ru.petr.songapp.data.repositories.SongsByCollectionsRepository
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.FullTextSearchResultItem
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes

private const val LOG_TAG = "SongListViewModel"

class SongListViewModel(private val repository: SongsByCollectionsRepository) : ViewModel() {

    private val _songsByCollections: MutableStateFlow<List<SongCollectionView>?> = MutableStateFlow(null)
    val songsByCollections = _songsByCollections.asStateFlow()
    private var _copySongsByCollection: List<SongCollectionView>? = null

    private val _searchIsActive = MutableStateFlow(false)
    val searchIsActive = _searchIsActive.asStateFlow()

    private val _collectionsScreenIsActive = MutableStateFlow(false)
    val collectionsScreenIsActive = _collectionsScreenIsActive.asStateFlow()

    private val _fullTextSearchIsActive = MutableStateFlow(false)
    val fullTextSearchIsActive = _fullTextSearchIsActive.asStateFlow()

    private val _fullTextSearchResult: MutableStateFlow<List<FullTextSearchResultItem>?> = MutableStateFlow(null)
    val fullTextSearchResult = _fullTextSearchResult.asStateFlow()

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
                            _copySongsByCollection = songCollectionViewList
                        }
                    }
                }
            }
        }

        navman.observeScreenChange { composableInstance ->
            if (composableInstance.composableResId == ComposableResourceIds.StartScreen) { // TODO здесь надо будет поменять на CollectionsScreen, когда переделаю нормально со стартовым экраном
                _collectionsScreenIsActive.value = true
            }
        }
    }

    fun insert(songDBModel: SongDBModel) = viewModelScope.launch { repository.insert(songDBModel) }

    fun updateOrGotoSong(id: Int) {
        navman.goto(
            composableResId = ComposableResourceIds.SongScreen,
            p = SongParams(
                songId = id,
                showType = SongShowTypes.VIEW
            )
        )
        _collectionsScreenIsActive.value = false
    }

    fun searchSongs(searchText: String) {
        if (searchText != "") {
            _copySongsByCollection?.toMutableList()?.let { songCollectionViews ->
                songCollectionViews.forEachIndexed { index, songCollectionView ->
                    val songCollectionViewNew: SongCollectionView = if (searchText.isDigitsOnly()) {
                        SongCollectionView(
                            songCollectionView.songCollection,
                            songCollectionView.songs.filter { searchText.toInt() == it.NumberInCollection }
                        )
                    } else {
                        SongCollectionView(
                            songCollectionView.songCollection,
                            songCollectionView.songs.filter { searchText.lowercase() in it.Name.lowercase() }
                        )
                    }
                    songCollectionViews[index] = songCollectionViewNew
                }

                _songsByCollections.value = songCollectionViews
                _searchIsActive.value = true
            }
        }
    }

    fun backFromSearch() {
        _songsByCollections.value = _copySongsByCollection
        _searchIsActive.value = false
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