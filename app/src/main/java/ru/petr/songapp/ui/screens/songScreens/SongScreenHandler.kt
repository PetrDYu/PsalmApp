package ru.petr.songapp.ui.screens.songScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongScreenParams
import ru.petr.songapp.ui.screens.songScreens.models.SongSettings
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes
import ru.petr.songapp.ui.screens.songScreens.songViewerScreen.SongViewerViewModel

@Composable
fun SongScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()

    val vm = composableInstance.viewmodel as SongScreenViewModel
    val p = composableInstance.parameters as SongScreenParams

    val songShowType = p.showType

    val song = vm.getSongById(p.songId).observeAsState().value
    val songFontSize = vm.fontSize.collectAsState().value
    val proModeIsActive = vm.proModeIsActive.collectAsState().value

    Scaffold (
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            if (song != null) {
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
            } else {
                // TODO Сделать что-то, если песня не найдена
            }

        }

    }

}

@Composable
fun PsalmTitle() {

}