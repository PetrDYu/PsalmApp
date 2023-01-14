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

@Composable
fun SongScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()
    Scaffold (
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            crm.RenderChildComposable(
                parentComposableId = composableInstance.id,
                composableResId = ComposableResourceIds.SongView,
                childComposableId = ComposableResourceIds.SongView,
                p = composableInstance.parameters
            )
        }

    }

}