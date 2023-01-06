package ru.petr.songapp.ui.screens.songScreens.models

import androidx.compose.runtime.Composable
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChordLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.RepeatLayer

@Composable
fun ShowAddingLayer(layer: ChunkLayer.AddingLayer) {
    when (layer) {
        is ChordLayer -> {
            // TODO
        }
        else -> {
            throw IllegalArgumentException("Incorrect adding layer class ${layer::class.simpleName}")
        }
    }
}

@Composable
fun ShowWrappingLayer(layer: ChunkLayer.WrappingLayer) {
    when (layer) {
        is RepeatLayer -> {
            // TODO
        }
        else -> {
            throw IllegalArgumentException("Incorrect wrapping layer class ${layer::class.simpleName}")
        }
    }
}