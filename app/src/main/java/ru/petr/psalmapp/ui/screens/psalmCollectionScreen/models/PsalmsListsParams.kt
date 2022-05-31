package ru.petr.psalmapp.ui.screens.psalmCollectionScreen.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams

class PsalmsListsParams(
    modifier: Modifier,
    data: Any? = null,
    var onChangeCollectionName: (String) -> Unit
) : ComposableParams(modifier = modifier, data = data)