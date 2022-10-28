package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers

import ru.petr.songapp.ui.screens.songScreens.models.parsing.TagAndAttrNames

class ChordLayer(override val layerChunkId: Int, override val layerId: Int = 0) : ChunkLayer.AddingLayer {

    companion object: ChunkLayerCompanion {
        override val layerTagName: String = TagAndAttrNames.CHORD_TAG._name
        override val layerName: String
            get() = TODO("Надо подумать, как динамически получать из ресурсов")
        override val layerType: ChunkLayerTypes = ChunkLayerTypes.MarkDataLayer
    }
}