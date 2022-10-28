package ru.petr.songapp.ui.screens.songScreens.songViewerScreen

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.songapp.ui.ComposableResourceIds

@Composable
fun SongScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState
    ) {
        crm.RenderChildComposable(
            parentComposableId = composableInstance.id,
            composableResId = ComposableResourceIds.SongView,
            childComposableId = ComposableResourceIds.SongView,
            p = composableInstance.parameters
        )
    }

}