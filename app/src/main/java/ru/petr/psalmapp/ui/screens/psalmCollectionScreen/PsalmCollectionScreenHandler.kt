package ru.petr.psalmapp.ui.screens.psalmCollectionScreen

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.wirespec.jetmagic.composables.crm
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.psalmapp.ui.ComposableResourceIds
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.models.PsalmsListsParams

@Composable
fun PsalmCollectionScreenHandler(composableInstance: ComposableInstance) {
//    Log.d("main_screen", "main handler start")
    val scaffoldState = rememberScaffoldState()
//    Log.d("main_screen", "create scaffoldState")
    var collectionName by remember { mutableStateOf("Сборники") }
    val onChangeCollectionName = { name: String -> collectionName = name}
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
            title = { Text(collectionName) },
            elevation = 10.dp
        )
        },
    ) {
        crm.RenderChildComposable(
            parentComposableId = composableInstance.id,
            composableResId = ComposableResourceIds.PsalmsLists,
            childComposableId = ComposableResourceIds.PsalmsLists,
            p = PsalmsListsParams(Modifier, onChangeCollectionName = onChangeCollectionName)
        )
    }

}