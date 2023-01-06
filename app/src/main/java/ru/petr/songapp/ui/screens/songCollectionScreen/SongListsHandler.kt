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
//    val vm = composableInstance.viewmodel as SongListViewModel
//    Log.d("main_screen", "create vm")
//    val songCollections = vm.songsByCollections.collectAsState().value
//    Log.d("main_screen", "create songCollections")

//    val searchViewActive by vm.searchViewActive.collectAsState()

    val p = composableInstance.parameters as SongsListsParams
    TabsContent(
        pagerState = p.pagerState,
        songCollections = p.songCollections,
        onSongNameClick = p.onSongNameClick
    )
}