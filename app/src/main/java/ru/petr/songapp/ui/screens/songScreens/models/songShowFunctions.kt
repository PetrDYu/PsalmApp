package ru.petr.songapp.ui.screens.songScreens.models

import ru.petr.songapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import ru.petr.songapp.data.models.room.songData.SongCollectionDBModel
import ru.petr.songapp.ui.screens.songScreens.models.songParts.SongPart
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.LineChunk
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.SongPartLine
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkText
import kotlin.math.max

@Composable
fun SongView(
    modifier: Modifier = Modifier,
    showType: SongShowTypes,
    song: Song,
    fontSize: Int,
) {
    Column(modifier) {
        for (part in song.mSongParts) {
            SongPartView(
                showType = showType,
                part = part,
                layerStack = song.mLayerStack,
                fontSize = fontSize,
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
    fontSize: Int,
) {
    when (part) {
        is SongPart.Chorus -> {
            ChorusView(
                showType = showType,
                chorus = part,
                layerStack = layerStack,
                fontSize = fontSize,
            )
        }
        is SongPart.Verse -> {
            VerseView(
                showType = showType,
                verse = part,
                layerStack = layerStack,
                fontSize = fontSize,
            )
        }
        is SongPart.Bridge -> {
            BridgeView(
                showType = showType,
                bridge = part,
                layerStack = layerStack,
                fontSize = fontSize,
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
    fontSize: Int,
) {
    Column(modifier) {
        Text("${stringResource(id = R.string.chorus_title)}:", fontSize = fontSize.sp)
        Column {
            for ((lineInd, line) in chorus.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) chorus.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != chorus.lines.lastIndex) chorus.lines[lineInd + 1] else null,
                    fontSize = fontSize,
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
    fontSize: Int,
) {
    Row(modifier) {
        Text("${verse.number}", fontSize = fontSize.sp)
        Column {
            for ((lineInd, line) in verse.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) verse.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != verse.lines.lastIndex) verse.lines[lineInd + 1] else null,
                    fontSize = fontSize,
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
    fontSize: Int,
) {
    Column(modifier) {
        Text("${stringResource(id = R.string.bridge_title)}:", fontSize = fontSize.sp)
        Column {
            for ((lineInd, line) in bridge.lines.withIndex()) {
                LineView(
                    showType = showType,
                    line = line,
                    layerStack = layerStack,
                    previousLine = if (lineInd != 0) bridge.lines[lineInd - 1] else null,
                    nextLine = if (lineInd != bridge.lines.lastIndex) bridge.lines[lineInd + 1] else null,
                    fontSize = fontSize,
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
    fontSize: Int,
) {
    SongTextAdaptiveContentLayout(modifier) {
        val chunksList = line.getChunksSplitByWords()
        for ((chunkInd, chunk) in chunksList.withIndex()) {
            ChunkView(
                showType = showType,
                chunk = chunk,
                layerStack = layerStack,
                previousChunk = if (chunkInd != 0) chunksList[chunkInd - 1] else null,
                nextChunk = if (chunkInd != chunksList.lastIndex) chunksList[chunkInd + 1] else null,
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
                },
                fontSize = fontSize,
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
    fontSize: Int,
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
                        isLayerMultiline,
                        fontSize,
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
                  fontSize: Int,
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
    Text(text = newChunkText.text, style = newTextStyle, fontSize = fontSize.sp)
}

@Composable
private fun SongTextAdaptiveContentLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, outerConstraints ->
        val rowElementsCounts = mutableListOf(0)
        val maxRowHeghts = mutableListOf(0)
        var rowNumber = 0
        var currentRowSize = 0
        val placeables = measurables.mapIndexed { index, measureable ->
            val placeable = measureable.measure(outerConstraints)
            if (currentRowSize != 0 &&
                ((currentRowSize + placeable.width) > outerConstraints.maxWidth)) {
                currentRowSize = 0
                rowElementsCounts.add(0)
                maxRowHeghts.add(0)
                rowNumber++
            }
            currentRowSize += placeable.width
            rowElementsCounts[rowNumber]++
            if (placeable.height > maxRowHeghts[rowNumber]) {
                maxRowHeghts[rowNumber] = placeable.height
            }
            placeable
        }

        val layoutHeight = maxRowHeghts.sum()

        layout(
            width = outerConstraints.constrainWidth(outerConstraints.maxWidth),
            height = outerConstraints.constrainHeight(layoutHeight)
        ) {
            var curPlaceableIndex = 0
            var xPosition = 0
            var yPosition = 0
            rowElementsCounts.forEachIndexed { rowIndex, count ->
                for(plIndex in curPlaceableIndex until count + curPlaceableIndex) {
                    placeables[plIndex].placeRelative(xPosition, yPosition)
                    xPosition += placeables[plIndex].width
                }
                xPosition = 0
                yPosition += maxRowHeghts[rowIndex]
                curPlaceableIndex += count
            }
        }
    }
}


@Preview
@Composable
fun SongPreview() {
    val songColl = SongCollectionDBModel(1, "Test", "Test")
//    val song = Song.getSong(, false, songColl)
//    SongView(showType = SongShowTypes.READ, song = )
}