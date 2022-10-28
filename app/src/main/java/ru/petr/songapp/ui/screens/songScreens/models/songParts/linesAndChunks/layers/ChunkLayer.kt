package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers

import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance

sealed interface ChunkLayer {
    val layerChunkId: Int
    val layerId: Int

    val layerType: ChunkLayerTypes
        get() = (this::class.companionObjectInstance as ChunkLayerCompanion).layerType

    sealed interface WrappingLayer: ChunkLayer
    sealed interface AddingLayer: ChunkLayer

    fun isSameWithTagNameLayerChunkIdAndLayerId(tagName: String, layerChunkId: Int, layerId: Int): Boolean {
        return ((this::class.companionObjectInstance as ChunkLayerCompanion).layerTagName == tagName) &&
                (this.layerChunkId == layerChunkId) &&
                (this.layerId == layerId)
    }

    fun isSameWithLayer(layer: ChunkLayer): Boolean {
        return this.isSameWithTagNameLayerChunkIdAndLayerId(
            (layer::class.companionObjectInstance as ChunkLayerCompanion).layerTagName,
            layer.layerChunkId,
            layer.layerId
        )
    }
}

interface ChunkLayerCompanion {
    val layerTagName: String
    val layerName: String
    val layerType: ChunkLayerTypes
}