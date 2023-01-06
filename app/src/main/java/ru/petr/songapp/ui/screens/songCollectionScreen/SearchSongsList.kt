package ru.petr.songapp.ui.screens.songCollectionScreen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SearchSongsParams
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongsListsParams

@Composable
fun SearchSongsListHandler(composableInstance: ComposableInstance) {
    val p = composableInstance.parameters as SearchSongsParams
    TabsContent(
        pagerState = p.pagerState,
        songCollections = p.songCollections,
        onSongNameClick = p.onSongNameClick,
        searchIsActive = true,
        fullTextSearchIsActive = p.fullTextSearchIsActive,
        fullTextSearchResult = p.fullTextSearchResult,
        onFullTextSearchClick = p.onFullTextSearchClick,
    )
}