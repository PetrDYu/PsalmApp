package ru.petr.songapp.ui.screens.songScreens.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams

class SongParams(
    modifier: Modifier = Modifier,
    data: Any? = null,
    val songId: Int,
    val showType: SongShowTypes,
) : ComposableParams(modifier = modifier, data = data)