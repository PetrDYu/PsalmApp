package ru.petr.songapp.ui.screens.songScreens.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection

class SongScreenParams(
    modifier: Modifier = Modifier,
    data: Any? = null,
    val songId: Int,
    val songCollection: SongCollection,
    val showType: SongShowTypes,
) : ComposableParams(modifier = modifier, data = data)