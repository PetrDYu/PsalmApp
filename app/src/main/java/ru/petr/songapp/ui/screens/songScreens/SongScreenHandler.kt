package ru.petr.songapp.ui.screens.songScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongScreenParams
import ru.petr.songapp.ui.screens.songScreens.models.SongSettings
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes

@Composable
fun SongScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()

    val vm = composableInstance.viewmodel as SongScreenViewModel
    val p = composableInstance.parameters as SongScreenParams

    val songShowType = p.showType

    val song = vm.getSongById(p.songId, p.songCollection).observeAsState().value
    val songFontSize = vm.fontSize.collectAsState().value
    val proModeIsActive = vm.proModeIsActive.collectAsState().value

    if (song != null) {
    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            val songNumber = song.mNumberInCollection.mNumber
            val songTitle by remember(songNumber) {
                mutableStateOf(song.mNumberInCollection.mCollection.mSongs.firstOrNull { it.numberInCollection == songNumber }?.name ?: "")
            }
            PsalmTitle(songNumber, songTitle, songFontSize)
         },
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
                when (songShowType) {
                    SongShowTypes.VIEW -> {
                        crm.RenderChildComposable(
                            parentComposableId = composableInstance.id,
                            composableResId = ComposableResourceIds.SongViewer,
                            childComposableId = ComposableResourceIds.SongViewer,
                            p = SongParams(
                                song = song,
                                songId = p.songId,
                                songSettings = SongSettings(
                                    songFontSize,
                                    proModeIsActive,
                                    vm::saveFontSizeSetting
                                )
                            )
                        )
                    }

                    SongShowTypes.EDIT -> {
                        crm.RenderChildComposable(
                            parentComposableId = composableInstance.id,
                            composableResId = ComposableResourceIds.SongEditor,
                            childComposableId = ComposableResourceIds.SongEditor,
                            p = SongParams(
                                song = song,
                                songId = p.songId,
                                songSettings = SongSettings(
                                    songFontSize,
                                    proModeIsActive,
                                    vm::saveFontSizeSetting
                                )
                            )
                        )
                    }
                }

            }

        }
    } else {
        // TODO Сделать что-то, если песня не найдена
    }

}

@Composable
fun PsalmTitle(number: Int, name: String, songFontSize: Int) {
    Card(backgroundColor = MaterialTheme.colors.background) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                },
                Modifier.padding(horizontal = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = Color(0xFF5E744B)
                )
            }
            
            Column(Modifier.padding(end = 20.dp)) {
                Text(
                    "$number",
                    fontSize = (songFontSize * 0.8).sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 2.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    name.uppercase(),
                    fontSize = (songFontSize * 0.8).sp,
                    modifier = Modifier.padding(bottom = 15.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}