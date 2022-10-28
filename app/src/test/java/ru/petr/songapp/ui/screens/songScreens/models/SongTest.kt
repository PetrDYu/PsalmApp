package ru.petr.songapp.ui.screens.songScreens.models

import org.junit.Test
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChordLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.RepeatLayer
import java.util.Collections.max

class SongTest {

    @Test
    fun `when AddingLayer add in layerStack it should add in addingLayers list`() {
        val stack = Song.LayerStack()
        stack.addLayer(ChordLayer(0))
        val a = 0
    }

    @Test
    fun `when WrappingLayer add in layerStack it should add in wrappingLayers list`() {
        val stack = Song.LayerStack()
        stack.addLayer(RepeatLayer(0, 2))
        val a = 0
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when WrappingLayer with same id add in layerStack exception should be thrown`() {
        val stack = Song.LayerStack()
        stack.addLayer(RepeatLayer(0, 2))
        stack.addLayer(RepeatLayer(0, 2))
        val a = 0
    }

    @Test
    fun `when WrappingLayer add in layerStack with non empty lists it should add in wrappingLayers list with previous id + 1`() {
        val stack = Song.LayerStack()
        stack.addLayer(RepeatLayer(0, 2))
        stack.addLayer(RepeatLayer(1, 2))
        val a = 0
    }
}