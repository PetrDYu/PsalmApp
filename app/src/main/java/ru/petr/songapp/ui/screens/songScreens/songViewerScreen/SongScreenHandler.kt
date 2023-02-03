package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.ComposableResourceIds
import ru.petr.songapp.ui.screens.songScreens.models.SongParams
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes

@Composable
fun SongScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()
    val songShowType = (composableInstance.parameters as SongParams).showType
    Scaffold (
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            when(songShowType) {
                SongShowTypes.VIEW -> {
                    crm.RenderChildComposable(
                        parentComposableId = composableInstance.id,
                        composableResId = ComposableResourceIds.SongViewer,
                        childComposableId = ComposableResourceIds.SongViewer,
                        p = composableInstance.parameters
                    )
                }
                SongShowTypes.EDIT -> {
                    crm.RenderChildComposable(
                        parentComposableId = composableInstance.id,
                        composableResId = ComposableResourceIds.SongEditor,
                        childComposableId = ComposableResourceIds.SongEditor,
                        p = composableInstance.parameters
                    )
                }
            }

        }

    }

}