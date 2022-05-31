package ru.petr.psalmapp.ui.screens.psalmScreen

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.psalmapp.ui.ComposableResourceIds

@Composable
fun PsalmScreenHandler(composableInstance: ComposableInstance) {
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState
    ) {
        crm.RenderChildComposable(
            parentComposableId = composableInstance.id,
            composableResId = ComposableResourceIds.PsalmView,
            childComposableId = ComposableResourceIds.PsalmView,
            p = composableInstance.parameters
        )
    }

}