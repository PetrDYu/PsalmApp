package ru.petr.songapp.ui.screens.songCollectionScreen

import androidx.compose.runtime.*
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongsListsParams

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