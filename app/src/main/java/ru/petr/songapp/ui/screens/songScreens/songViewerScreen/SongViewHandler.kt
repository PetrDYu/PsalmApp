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
import ru.petr.songapp.data.models.songData.SongWithCollectionFromDB
import ru.petr.songapp.ui.screens.songScreens.models.SongParams

@Composable
fun SongViewHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as SongViewViewModel
    val p = composableInstance.parameters as SongParams
    val song by vm.getSongById(p.songId).observeAsState()
    SongView(song = song)
}

@Composable
fun SongView(song: SongWithCollectionFromDB?) {
    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    ) {
        if (song == null) {
            Text("Ошибка в процессе загрузки")
        } else {
            Text(song.body)
        }
    }
}