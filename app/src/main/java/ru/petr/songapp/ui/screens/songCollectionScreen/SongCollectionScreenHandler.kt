package ru.petr.songapp.ui.screens.songCollectionScreen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import dev.wirespec.jetmagic.navigation.navman
import kotlinx.coroutines.launch
import ru.petr.songapp.R
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songCollectionScreen.models.FullTextSearchResultItem
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SearchSongsParams
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongsListsParams

@Composable
fun SongCollectionScreenHandler(composableInstance: ComposableInstance) {
//    Log.d("main_screen", "main handler start")
    val scaffoldState = rememberScaffoldState()
//    Log.d("main_screen", "create scaffoldState")
    var collectionName by remember { mutableStateOf("Сборники") }

    val vm = composableInstance.viewmodel as SongListViewModel

    val collectionsScreenIsActive by vm.collectionsScreenIsActive.collectAsState()

    val songCollections = vm.songsByCollections.collectAsState().value

    val searchIsActive = vm.searchIsActive.collectAsState().value
    val fullTextSearchIsActive = vm.fullTextSearchIsActive.collectAsState().value
    val fullTextSearchResult = vm.fullTextSearchResult.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var searchText by remember { mutableStateOf("") }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
            title = { Text(collectionName) },
            elevation = 10.dp
            )
        },
    ) {
        Box(Modifier.padding(it)) {
            CollectionsScreen(
                songCollections = songCollections,
                onChangeCollectionName = { name: String -> collectionName = name},
                onSearch = vm::searchSongs,
//                composableId = composableInstance.id,
                searchIsActive = searchIsActive,
                fullTextSearchIsActive = fullTextSearchIsActive,
                fullTextSearchResult = fullTextSearchResult,
                onFullTextSearchClick = {},
                searchText = searchText,
                onChangeSearchText = { newText ->
                    searchText = newText
                }
            ) { id ->
                vm.updateOrGotoSong(id)
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        }
    }

    BackHandler(searchIsActive && collectionsScreenIsActive) {
        vm.backFromSearch()
        searchText = ""
    }

}

@Composable
fun CollectionsScreen(songCollections: List<SongCollectionView>?,
                      onChangeCollectionName: (String)->Unit,
                      onSearch: (searchText: String) -> Unit,
//                      composableId: String,
                      searchIsActive: Boolean,
                      fullTextSearchIsActive: Boolean,
                      fullTextSearchResult: List<FullTextSearchResultItem>? = listOf(),
                      onFullTextSearchClick: (collectionId: Int) -> Unit,
                      searchText: String,
                      onChangeSearchText: (newText: String) -> Unit,
                      onSongNameClick: (id:Int) -> Unit,
) {
    if (songCollections.isNullOrEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(stringResource(id = R.string.collections_not_added))
        }

    } else {
        Column {
            val pagerState = rememberPagerState(pageCount = songCollections.size)
            onChangeCollectionName(songCollections[pagerState.currentPage].songCollection.name)
            Box(Modifier.weight(1f)){
                if (!searchIsActive) {
                    /*crm.RenderChildComposable(
                        parentComposableId = composableId,
                        composableResId = ComposableResourceIds.SongsLists,
                        childComposableId = ComposableResourceIds.SongsLists,
                        p = SongsListsParams(
                            Modifier,
                            onSongNameClick = onSongNameClick,
                            songCollections = songCollections,
                            pagerState = pagerState
                        )
                    )*/
                    TabsContent(
                        pagerState = pagerState,
                        songCollections = songCollections,
                        onSongNameClick = onSongNameClick
                    )
                } else {
                    /*crm.RenderChildComposable(
                        parentComposableId = composableId,
                        composableResId = ComposableResourceIds.SearchSongs,
                        childComposableId = ComposableResourceIds.SearchSongs,
                        p = SearchSongsParams(
                            Modifier,
                            onSongNameClick = onSongNameClick,
                            songCollections = songCollections,
                            pagerState = pagerState,
                            fullTextSearchIsActive = fullTextSearchIsActive,
                            fullTextSearchResult = fullTextSearchResult?: listOf(),
                            onFullTextSearchClick = onFullTextSearchClick,
                        )
                    )*/
                    TabsContent(
                        pagerState = pagerState,
                        songCollections = songCollections,
                        onSongNameClick = onSongNameClick,
                        searchIsActive = true,
                        fullTextSearchIsActive = fullTextSearchIsActive,
                        fullTextSearchResult = fullTextSearchResult?: listOf(),
                        onFullTextSearchClick = onFullTextSearchClick,
                    )
                }

            }
            Tabs(tabs = songCollections, pagerState = pagerState)
//            TabsContent(Modifier.weight(1f), pagerState = pagerState, songCollections = songCollections, onSongNameClick = onSongNameClick)
//            Tabs(tabs = songCollections, pagerState = pagerState)
            SearchSongBar(
                searchText = searchText,
                onChangeSearchText = onChangeSearchText,
                onSearchButtonClick = {
                    onSearch(searchText)
                }
            )
        }
    }
}

@Composable
fun Tabs(tabs: List<SongCollectionView>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = {},
                text = { Text(tab.songCollection.shortName) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}