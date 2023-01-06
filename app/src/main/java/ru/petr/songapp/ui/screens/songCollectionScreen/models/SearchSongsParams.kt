package ru.petr.songapp.ui.screens.songCollectionScreen.models

import androidx.compose.ui.Modifier
import com.google.accompanist.pager.PagerState
import dev.wirespec.jetmagic.models.ComposableParams

class SearchSongsParams(
    modifier: Modifier,
    data: Any? = null,
    onSongNameClick: (id:Int) -> Unit,
    pagerState: PagerState,
    songCollections: List<SongCollectionView>,
    var fullTextSearchIsActive: Boolean = false,
    var fullTextSearchResult: List<FullTextSearchResultItem> = listOf(),
    var onFullTextSearchClick: (collectionId: Int)->Unit,
) : SongsListsParams(
    modifier = modifier,
    data = data,
    onSongNameClick = onSongNameClick,
    pagerState = pagerState,
    songCollections = songCollections
)
