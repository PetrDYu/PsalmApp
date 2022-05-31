package ru.petr.psalmapp.ui.screens.psalmScreen.models

import androidx.compose.ui.Modifier
import dev.wirespec.jetmagic.models.ComposableParams

class PsalmParams(
    modifier: Modifier,
    data: Any? = null,
    val psalmId: Int
) : ComposableParams(modifier = modifier, data = data)