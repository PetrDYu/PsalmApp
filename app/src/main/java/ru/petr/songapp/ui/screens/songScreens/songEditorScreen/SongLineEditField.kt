package ru.petr.songapp.ui.screens.songScreens.songEditorScreen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.sp
import ru.petr.songapp.ui.screens.songScreens.models.ChunkView
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.SongShowTypes
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.LineChunk
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer

@Composable
fun SongLineEditField(chunks: List<LineChunk>,
                      onValueChange: (List<LineChunk>) -> Unit,
                      modifier: Modifier = Modifier,
                      layerStack: Song.LayerStack,
                      isLayerMultiline: (layer: ChunkLayer) -> Boolean,
                      fontSize: Int,
                      decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
                       @Composable { innerTextField -> innerTextField() }
) {
    val focusRequester = FocusRequester()
    decorationBox {
        Row() {
            for (chunk in chunks) {
                val text = chunk.text
                requireNotNull(text)
                Text(
                    text = text.text,
                    fontSize = fontSize.sp
                )
            }
        }
    }
}