package ru.petr.songapp.ui.screens.songCollectionScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import ru.petr.songapp.R
import ru.petr.songapp.data.models.room.songData.dao.ShortSong
import ru.petr.songapp.songCollections
import ru.petr.songapp.songs
import ru.petr.songapp.ui.screens.songCollectionScreen.models.FullTextSearchResultItem
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollectionView
import ru.petr.songapp.ui.theme.SongAppTheme

@Composable
fun TabsContent(modifier: Modifier = Modifier,
                pagerState: PagerState,
                songCollections: List<SongCollectionView>,
                onSongNameClick: (id:Int) -> Unit,
                searchIsActive: Boolean = false,
                fullTextSearchIsActive: Boolean = false,
                fullTextSearchResult: List<FullTextSearchResultItem> = listOf(),
                onFullTextSearchClick: (collectionId: Int)->Unit = {},
) {
    HorizontalPager(state = pagerState, modifier) { pageNumber ->
        var songList = songCollections[pageNumber].songs.toList()
        if (songList.isEmpty()){
            songList = listOf()
        }
        Box(/*Modifier.background(Color.White)*/) {
            SongList(
                songs = songList,
                onSongNameClick = onSongNameClick,
                searchIsActive = searchIsActive,
                fullTextSearchIsActive = fullTextSearchIsActive,
                fullTextSearchResult = fullTextSearchResult,
                onFullTextSearchClick = { onFullTextSearchClick(songCollections[pageNumber].songCollection.id) }
            )
        }

    }
}


@Composable
fun SongList(songs: List<ShortSong>,
             onSongNameClick: (id:Int) -> Unit,
             searchIsActive: Boolean,
             fullTextSearchIsActive: Boolean,
             fullTextSearchResult: List<FullTextSearchResultItem>,
             onFullTextSearchClick: () -> Unit,
){
    if (songs.isEmpty()){
        if (searchIsActive) {
            Text(stringResource(
                id = R.string.not_found_songs_in_collection),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
        } else {
            Text(stringResource(
                id = R.string.not_added_songs_in_collection),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    } else {
        LazyColumn(
            Modifier
                .fillMaxSize()
        ) {
            items(songs.size) { index ->
                Row(
                    Modifier
                        .clickable { onSongNameClick(songs[index].Id) }
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .fillMaxWidth()

                ) {
                    Text(
                        "${songs[index].NumberInCollection}. ",
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.secondaryVariant,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        songs[index].Name,
                        fontSize = 20.sp
                    )
                }

                if (index != songs.size - 1 || fullTextSearchIsActive)
                    Divider(Modifier.padding(horizontal = 20.dp))
            }

            if (searchIsActive) {
                if (!fullTextSearchIsActive) {
                    items(1) {
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 40.dp, vertical = 10.dp)
                                .fillMaxWidth()
                                .height(40.dp),
                            onClick = { onFullTextSearchClick() }
                        ) {
                            Text(stringResource(id = R.string.search_by_full_text))
                        }
                    }
                } else {
                    items(fullTextSearchResult.size) { index ->
                        Text(
                            "${fullTextSearchResult[index].song.NumberInCollection}. ${fullTextSearchResult[index].song.Name}",
                            Modifier
                                .clickable { onSongNameClick(fullTextSearchResult[index].song.Id) }
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            fontSize = 20.sp
                        )
                        if (index != fullTextSearchResult.size - 1)
                            Divider()
                    }
                }
            }
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
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = searchText,
            onValueChange = onChangeSearchText,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchButtonClick()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            placeholder = { Text(text = stringResource(id = R.string.search_bar_placeholder)) },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .weight(1f),
            singleLine = true,
//            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
        )
//        var offset by remember { mutableStateOf(0f) }
//        var wordoffset by remember {
//            mutableStateOf(0f to 0f)
//        }
//        Column() {
//            Text("offset: $offset, word: ${wordoffset.first}: ${wordoffset.second}")
//            BasicTextField(value = "long1 long2 long3 long4 long5 long6 long7 long8 long9 long10 long11 long12 text",
//                onValueChange = {},
//                singleLine = true,
//                onTextLayout = {layoutResult ->
//                    val boundary = layoutResult.getWordBoundary(0)
//                    val path = layoutResult.getPathForRange(boundary.start, boundary.end)
//                    wordoffset = path.getBounds().left to path.getBounds().right
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .scrollable(
//                        orientation = Orientation.Horizontal,
//                        state = rememberScrollableState { delta ->
//                            offset += delta
//                            delta
//                        }
//                    )
//            )
//        }

        IconButton(
            onClick = {
                onSearchButtonClick()
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .padding(end = 6.dp),
//                tint = Color(243,  244, 236)
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
        SongList(songs, {}, false, false, listOf(), {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview(){
    SearchSongBar(Modifier, "test", {}, {})
}