package ru.petr.songapp.ui.screens.songScreens.models

import ru.petr.songapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.ui.screens.songScreens.models.songParts.SongPart
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.LineChunk
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.SongPartLine
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkText

@Composable
fun SongView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    song: Song,
) {
    Column(modifier) {
        for (part in song.mSongParts) {
            SongPartView(
                showType = showType,
                part = part,
                layerStack = song.mLayerStack
            )
        }
    }
}

@Composable
fun SongPartView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    part: SongPart,
    layerStack: Song.LayerStack,
) {
    when (part) {
        is SongPart.Chorus -> {
            ChorusView(
                showType = showType,
                chorus = part,
                layerStack = layerStack,
            )
        }
        is SongPart.Verse -> {
            VerseView(
                showType = showType,
                verse = part,
                layerStack = layerStack,
            )
        }
        is SongPart.Bridge -> {
            BridgeView(
                showType = showType,
                bridge = part,
                layerStack = layerStack,
            )
        }
    }
}

@Composable
fun ChorusView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    chorus: SongPart.Chorus,
    layerStack: Song.LayerStack,
) {
    Column(modifier) {
        Text("${stringResource(id = R.string.chorus_title)}:")
        Column {
            for ((lineInd, line) in chorus.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) chorus.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != chorus.lines.lastIndex) chorus.lines[lineInd + 1] else null,
                )
            }
        }
    }
}

@Composable
fun VerseView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    verse: SongPart.Verse,
    layerStack: Song.LayerStack,
) {
    Row(modifier) {
        Text("${verse.number}")
        Column {
            for ((lineInd, line) in verse.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) verse.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != verse.lines.lastIndex) verse.lines[lineInd + 1] else null,
                )
            }
        }
    }
}

@Composable
fun BridgeView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    bridge: SongPart.Bridge,
    layerStack: Song.LayerStack,
) {
    Column(modifier) {
        Text("${stringResource(id = R.string.bridge_title)}:")
        Column {
            for ((lineInd, line) in bridge.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) bridge.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != bridge.lines.lastIndex) bridge.lines[lineInd + 1] else null,
                )
            }
        }
    }
}

@Composable
fun LineView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    line: SongPartLine,
    layerStack: Song.LayerStack,
    previousLine: SongPartLine?,
    nextLine: SongPartLine?,
) {
    Row(modifier) {
        for ((chunkInd, chunk) in line.chunks.withIndex()) {
            ChunkView(
                showType = showType,
                chunk = chunk,
                layerStack = layerStack,
                previousChunk = if (chunkInd != 0) line.chunks[chunkInd - 1] else null,
                nextChunk = if (chunkInd != line.chunks.lastIndex) line.chunks[chunkInd + 1] else null,
                isLayerMultiline = { layer ->
                    var result = false
                    previousLine?.let { line ->
                        if (line.chunks.isNotEmpty())
                        {
                            result = line.chunks[line.chunks.lastIndex].hasSameLayer(layer)
                        }
                    }
                    nextLine?.let { line ->
                        if (line.chunks.isNotEmpty())
                        {
                            result = result || line.chunks[0].hasSameLayer(layer)
                        }
                    }
                    result
                }
            )
        }
    }
}

@Composable
fun ChunkView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    layerStack: Song.LayerStack,
    chunk: LineChunk,
    previousChunk: LineChunk? = null,
    nextChunk: LineChunk? = null,
    isLayerMultiline: (layer: ChunkLayer) -> Boolean,
) {
    Box(modifier = modifier) {
        when (showType) {
            SongShowTypes.READ -> {
                Column() {
                    for (layer in layerStack.activeAddingLayers) {
                        val chunkAddingLayer = chunk.getSimilarLayer(layer)
                        if (chunkAddingLayer != null) {
                            ShowAddingLayer(layer = layer)
                        } else {
                            // TODO здесь надо будет сделать заполнение для отсутствующих в конкретном chunk'е слоёв
                        }
                    }

                    ChunkTextView(
                        chunk.text,
                        chunk.layers
                            .filter { layer -> null != layerStack.activeWrappingLayers.find{ it.isSimilarWithLayer(layer) } }
                            .map { it as ChunkLayer.WrappingLayer },
                        {layer -> previousChunk?.hasSameLayer(layer) ?: false },
                        {layer -> nextChunk?.hasSameLayer(layer) ?: false },
                        isLayerMultiline
                    )
                }
            }
            SongShowTypes.EDIT -> {}
        }
    }
}

@Composable
fun ChunkTextView(text: ChunkText?,
                  chunkWrappingLayers: List<ChunkLayer.WrappingLayer>,
                  previousChunkHasSameLayer: (layer: ChunkLayer.WrappingLayer) -> Boolean,
                  nextChunkHasSameLayer: (layer: ChunkLayer.WrappingLayer) -> Boolean,
                  isLayerMultiline: (layer: ChunkLayer.WrappingLayer) -> Boolean,
) {
    require(text != null)
    var newChunkText: ChunkText = text
    var newTextStyle = LocalTextStyle.current.copy()
    chunkWrappingLayers.forEach {
        val textAndStyle = it.modifyTextAndStyle(
            newChunkText,
            newTextStyle,
            isStart = !previousChunkHasSameLayer(it),
            isEnd = !nextChunkHasSameLayer(it),
            isMultiline = isLayerMultiline(it),
            LocalContext.current,
        )
        newChunkText = textAndStyle.first
        newTextStyle = textAndStyle.second
    }
    Text(newChunkText.text, style = newTextStyle)
}


@Preview
@Composable
fun SongPreview() {
    val songColl = SongCollectionDBModel(1, "Test", "Test")
//    val song = Song.getSong(, false, songColl)
//    SongView(showType = SongShowTypes.READ, song = )
}