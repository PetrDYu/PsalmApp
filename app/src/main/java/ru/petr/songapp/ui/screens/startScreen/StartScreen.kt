package ru.petr.songapp.ui.screens.startScreen

import androidx.compose.runtime.Composable
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import dev.wirespec.jetmagic.navigation.navman
import ru.petr.songapp.ui.ComposableResourceIds

@Composable
fun StartScreenHandler(composableInstance: ComposableInstance) {
    crm.RenderChildComposable(
        parentComposableId = composableInstance.id,
        composableResId = ComposableResourceIds.MainScreen,
        childComposableId = ComposableResourceIds.MainScreen
    )
//    navman.goto(composableResId = ComposableResourceIds.SongCollectionsScreen)
}