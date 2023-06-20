package ru.petr.songapp.ui.screens.songCollectionScreen.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams

class SongCollectionScreenParams(
    modifier: Modifier,
    data: Any? = null,
    val songCollectionId: Int,
) : ComposableParams(modifier = modifier, data = data)