package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers

import org.junit.Assert
import org.junit.Test


internal class ChunkLayerTypesTest {

    @Test
    fun testTest() {
        Assert.assertEquals(ChunkLayerTypes.ContinuousDataLayer, ChunkLayerTypes.getLayerTypeByName("repeat"))
    }
}