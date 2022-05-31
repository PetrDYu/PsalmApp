package ru.petr.psalmapp.ui.screens.psalmScreen

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableInstance
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.ui.screens.psalmScreen.models.PsalmParams

@Composable
fun PsalmViewHandler(composableInstance: ComposableInstance) {
    val vm = composableInstance.viewmodel as PsalmViewViewModel
    val p = composableInstance.parameters as PsalmParams
    val psalm by vm.getPsalmById(p.psalmId).observeAsState()
    PsalmView(psalm = psalm)
}

@Composable
fun PsalmView(psalm: Psalm?) {
    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    ) {
        if (psalm == null) {
            Text("Ошибка в процессе загрузки")
        } else {
            Text(psalm.body)
        }
    }
}