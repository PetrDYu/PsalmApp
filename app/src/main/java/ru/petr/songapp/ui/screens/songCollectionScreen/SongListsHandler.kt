package ru.petr.songapp.ui.screens.songCollectionScreen

import android.content.res.Configuration
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.google.accompanist.pager.*
import dev.wirespec.jetmagic.models.ComposableInstance
import kotlinx.coroutines.launch
import ru.petr.songapp.*
import ru.petr.songapp.data.models.songData.dao.ShortSong
import ru.petr.songapp.data.repositories.utils.SongCollectionFlow
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongsListsParams
import ru.petr.songapp.ui.theme.SongAppTheme

@Composable
fun SongsListsHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as SongListViewModel
    Log.d("main_screen", "create vm")
    val songCollections = vm.songsByCollections.collectAsState().value
    Log.d("main_screen", "create songCollections")

    val searchViewActive by vm.searchViewActive.collectAsState()

    val p = composableInstance.parameters as SongsListsParams
    SongsLists(
        songCollections = songCollections,
        onChangeCollectionName = p.onChangeCollectionName,
        onSearch = vm::searchSongs
    ) { id ->
        vm.updateOrGotoSong(id)
    }
    BackHandler(searchViewActive) {
        vm.backFromSearch()
    }
}

@Composable
fun SongsLists(songCollections: List<SongCollectionView>?,
               onChangeCollectionName: (String)->Unit,
               onSearch: (searchText: String) -> Unit,
               onSongNameClick: (id:Int) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    if (songCollections.isNullOrEmpty()) {
        Text("Ни одного сборника ещё не добавлено")
    } else {
        Column {
            val pagerState = rememberPagerState(pageCount = songCollections.size)
            onChangeCollectionName(songCollections[pagerState.currentPage].songCollection.name)
            TabsContent(Modifier.weight(1f), pagerState = pagerState, songCollections = songCollections, onSongNameClick = onSongNameClick)
            Tabs(tabs = songCollections, pagerState = pagerState)
            SearchSongBar(
                searchText = searchText,
                onChangeSearchText = { newText ->
                    searchText = newText
                },
                onSearchButtonClick = {
                    onSearch(searchText)
                }
            )
        }
    }
}

@Composable
fun SongList(songs: List<ShortSong>, onSongNameClick: (id:Int) -> Unit){
    if (songs.isEmpty()){
        Text("В сборник не добавлено ни одного псалма")
    } else {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 0.dp)
        ) {
            items(songs.size) { index ->
                Text(
                    "${songs[index].NumberInCollection}. ${songs[index].Name}",
                    Modifier
                        .clickable { onSongNameClick(songs[index].Id) }
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp
                )
                if (index != songs.size - 1)
                    Divider()
            }
        }
    }
}

@Composable
fun TabsContent(modifier: Modifier = Modifier,
                pagerState: PagerState,
                songCollections: List<SongCollectionView>,
                onSongNameClick: (id:Int) -> Unit) {
    HorizontalPager(state = pagerState, modifier) { pageNumber ->
        var songList = songCollections[pageNumber].songs.toList()
        if (songList.isEmpty()){
            songList = listOf()
        }
        SongList(songs = songList, onSongNameClick = onSongNameClick)
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

@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val pagerState = rememberPagerState(pageCount = songCollections.size)
    Tabs(tabs = songCollections, pagerState = pagerState)
}

@Composable
fun SearchSongBar(modifier: Modifier = Modifier, searchText: String, onChangeSearchText: (newText: String)->Unit, onSearchButtonClick: ()->Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextField(
            value = searchText,
            onValueChange = onChangeSearchText,
            placeholder = {Text(text = "Название или номер псалма")},
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .weight(1f),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        )
        IconButton(onClick = onSearchButtonClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .padding(end = 6.dp),
                tint = Color.Blue
            )
        }
    }

}

@Composable
fun MainTopBar(name: String) {
    TopAppBar() {
        Text(text = name)
    }
}

@Composable
fun NavBottomBar(){
    BottomAppBar() {

    }
}

@Composable
public fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SongAppTheme {
//        Main(ComposableInstance("123", composableResId = "main_screen"))
    }
}

@Preview(
    name="SongList",
    showBackground = true,
//    heightDp = 50,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SongListPreview(){
    SongAppTheme {
        SongList(songs = songs) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview(){
    SearchSongBar(Modifier, "test", {}, {})
}