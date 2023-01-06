package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks

import ru.petr.songapp.ui.screens.songScreens.models.parsing.TagAndAttrNames
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayerCompanion
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayerTypes
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkText
import kotlin.reflect.full.companionObjectInstance

class LineChunk {
    var text: ChunkText? = null
        set(value) = if (field == null) field = value else throw IllegalArgumentException("Text in chunk should be null while assignment!")

    var layers: List<ChunkLayer> = listOf()
        private set

    val hasMarkDataLayer: Boolean
        get() = null != layers.find { chunkLayer ->
            chunkLayer.layerType == ChunkLayerTypes.MarkDataLayer
        }

    fun setLayersIfNotSet(layers: List<ChunkLayer>): Boolean {
        var result = false
        if (this.layers.isEmpty()) {
            this.layers = layers
            result = true
        }
        return result
    }

    fun hasSameLayer(layer: ChunkLayer): Boolean {
        return null != this.layers.find { chunkLayer -> chunkLayer.isSameWithLayer(layer) }
    }

    fun hasSimilarLayer(layer: ChunkLayer): Boolean {
        return null != this.layers.find { chunkLayer -> chunkLayer.isSimilarWithLayer(layer) }
    }

    fun getSimilarLayer(layer: ChunkLayer): ChunkLayer? {
        return this.layers.find { chunkLayer -> chunkLayer.isSimilarWithLayer(layer) }
    }

    fun hasSameLayer(tagName: String, layerChunkId: Int, layerId: Int): Boolean {
        return null != this.layers.find { chunkLayer -> chunkLayer.isSameWithTagNameLayerChunkIdAndLayerId(tagName, layerChunkId, layerId) }
    }

    fun isEmpty(): Boolean {
        return ((text == null && !hasMarkDataLayer) && layers.isEmpty())
    }

    fun hasSameLayers(chunk: LineChunk): Boolean {
        var result = true
        if (layers.size != chunk.layers.size) {
            result = false
        }
        if (result) {
            val thisChunkLayers = this.layers.toMutableList()
            val otherChunkLayers = chunk.layers.toMutableList()

            for (layerInd in 0..thisChunkLayers.lastIndex) {
                val layerPosInOtherChunkLayers = otherChunkLayers.indexOfFirst { layer ->
                    layer.isSameWithLayer(thisChunkLayers[layerInd])
                }
                if (layerPosInOtherChunkLayers == -1) {
                    result = false
                    break
                } else {
                    otherChunkLayers.removeAt(layerPosInOtherChunkLayers)
                }
            }
        }
        return result
    }

    fun copy(text: ChunkText? = this.text, layers: List<ChunkLayer> = this.layers): LineChunk {
        val chunk =  LineChunk()
        chunk.text = text
        chunk.layers = layers
        return chunk
    }
}