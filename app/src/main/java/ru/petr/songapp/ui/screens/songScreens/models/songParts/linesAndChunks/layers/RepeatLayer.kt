package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers

import ru.petr.songapp.ui.screens.songScreens.models.parsing.TagAndAttrNames

class RepeatLayer(override val layerChunkId: Int, val repRate: Int, override val layerId: Int=0): ChunkLayer.WrappingLayer {

    companion object: ChunkLayerCompanion {
        // Tag name according this layer
        override val layerTagName: String = TagAndAttrNames.REPEAT_TAG._name
        // Layer name to show on screen in layer stack
        override val layerName: String
            get() = TODO("Not yet implemented")
        override val layerType: ChunkLayerTypes = ChunkLayerTypes.getLayerTypeByName(layerTagName)
    }
}