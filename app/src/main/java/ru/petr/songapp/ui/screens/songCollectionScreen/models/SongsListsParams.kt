package ru.petr.songapp.ui.screens.songCollectionScreen.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams

class SongsListsParams(
    modifier: Modifier,
    data: Any? = null,
    var onChangeCollectionName: (String) -> Unit
) : ComposableParams(modifier = modifier, data = data)