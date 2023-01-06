package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes

@Composable
fun SongViewerHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as SongViewerViewModel
    val p = composableInstance.parameters as SongParams
    val song by vm.getSongById(p.songId).observeAsState()
    SongViewer(song = song)
}

@Composable
fun SongViewer(song: Song?) {
    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    ) {
        if (song == null) {
            Text("Ошибка в процессе загрузки")
        } else {
            ru.petr.songapp.ui.screens.songScreens.models.SongView(showType = SongShowTypes.READ, song = song)
        }
    }
}