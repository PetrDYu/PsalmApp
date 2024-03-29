package ru.petr.songapp.ui.screens.songScreens.models.parsing

import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.*

object ChunkLayersBuilder {
    fun modifyLayerList(
        layerStack: Song.LayerStack,
        currentLayers: SongPartBuilder.CurrentLayersHolder,//MutableList<ChunkLayer>,
        tagName: String,
        attributes: Map<String, String>,
        onFindMarkDataTag: (newLayer: ChunkLayer) -> Unit,
        onFindClosedTag: (newLayer: ChunkLayer) -> Unit,
    ) {
        val layerChunkId = attributes
            .getOrElse(TagAndAttrNames.ID_ATTR._name) {
                throw IllegalArgumentException("Attribute \"id\" is not presented in attributes for $tagName layer")
            }.toInt()

        val layerId = attributes.getOrDefault(TagAndAttrNames.LAYER_ID_ATTR._name, "0").toInt()

        val mutableAttributes = attributes.toMutableMap()

        when (val layerType = ChunkLayerTypes.getLayerTypeByName(tagName)) {
            ChunkLayerTypes.MarkDataLayer -> {
                processMarkDataTag(
                    layerStack,
                    tagName,
                    layerChunkId,
                    layerId,
                    mutableAttributes,
                    onFindMarkDataTag
                )
            }
            ChunkLayerTypes.ContinuousDataLayer -> {
                val layerIsOpening = mutableAttributes
                    .getOrElse(TagAndAttrNames.IS_OPENING_ATTR._name) {
                        throw IllegalArgumentException("Attribute \"is_opening\" is not presented in attributes for $tagName layer")
                    }.toBoolean()
                if (layerIsOpening) {
                    processOpenContinuousDataTag(
                        layerStack,
                        currentLayers,
                        tagName,
                        layerChunkId,
                        layerId,
                        mutableAttributes
                    )
                } else {
                    processCloseContinuousDataTag(
                        layerStack,
                        currentLayers,
                        tagName,
                        layerChunkId,
                        layerId,
                        mutableAttributes,
                        onFindClosedTag
                    )
                }
            }
            else -> {
                throw IllegalArgumentException("Unknown layer type $layerType")
            }
        }
    }

    private fun processMarkDataTag(layerStack: Song.LayerStack,
                                   tagName: String,
                                   layerChunkId: Int,
                                   layerId: Int,
                                   attributes: Map<String, String>,
                                   onFindMarkDataTag: (newLayer: ChunkLayer) -> Unit) {
        val layer = getChunkLayer(tagName, layerChunkId, layerId, attributes)
        onFindMarkDataTag(layer)
        layerStack.addLayer(layer)
    }

    private fun processOpenContinuousDataTag(layerStack: Song.LayerStack,
                                             currentLayers: SongPartBuilder.CurrentLayersHolder,//MutableList<ChunkLayer>,
                                             tagName: String,
                                             layerChunkId: Int,
                                             layerId: Int,
                                             attributes: Map<String, String>) {
        if (checkLayerWithNameAndIdInLayersList(tagName, layerChunkId, layerId, currentLayers.layers)) {
            throw IllegalArgumentException("$tagName layer with id=$layerChunkId already in currentLayers")
        }
        val layer = getChunkLayer(tagName, layerChunkId, layerId, attributes)
        layerStack.addLayer(layer)
        currentLayers.add(layer)
    }

    private fun processCloseContinuousDataTag(layerStack: Song.LayerStack,
                                              currentLayers: SongPartBuilder.CurrentLayersHolder,//MutableList<ChunkLayer>,
                                              tagName: String,
                                              layerChunkId: Int,
                                              layerId: Int,
                                              attributes: Map<String, String>,
                                              onFindClosedTag: (newLayer: ChunkLayer) -> Unit,
    ) {
        if (!checkLayerWithNameAndIdInLayersList(tagName, layerChunkId, layerId, currentLayers.layers)) {
            throw IllegalArgumentException("$tagName layer with id=$layerChunkId doesn't exist in currentLayers")
        }

        val layer = getChunkLayer(tagName, layerChunkId, layerId, attributes)
        onFindClosedTag(layer)

        currentLayers.removeIf { chunkLayer ->
            chunkLayer.isSameWithTagNameLayerChunkIdAndLayerId(tagName, layerChunkId, layerId)
        }
    }

    private fun checkLayerWithNameAndIdInLayersList(tagName: String, layerChunkId: Int, layerId: Int, currentLayers: List<ChunkLayer>): Boolean {
        return null != currentLayers.find { layer ->
            layer.isSameWithTagNameLayerChunkIdAndLayerId(tagName, layerChunkId, layerId)
        }
    }

    private fun getChunkLayer(tagName: String,
                              layerChunkId: Int,
                              layerId: Int,
                              attributes: Map<String, String>): ChunkLayer {
        return when(tagName) {
            // Mark data layers
            ChordLayer.layerTagName -> {
                buildChordLayer(layerChunkId, attributes)
            }

            // Continuous data layers
            RepeatLayer.layerTagName -> {
                buildRepeatLayer(layerChunkId, attributes)
            }

            else -> {
                throw IllegalArgumentException("Unknown layer tag $tagName")
            }
        }
    }

    private fun buildChordLayer(layerChunkId: Int, attributes: Map<String, String>): ChordLayer {
        return ChordLayer(layerChunkId)
    }

    private fun buildRepeatLayer(layerChunkId: Int, attributes: Map<String, String>): RepeatLayer {
        val repLayerRepRate = attributes
            .getOrElse(TagAndAttrNames.REP_RATE_ATTR._name) {
                throw IllegalArgumentException("\"${TagAndAttrNames.REP_RATE_ATTR._name}\" attribute is not presented in attributes for repeat layer")
            }.toInt()
        return RepeatLayer(layerChunkId, repLayerRepRate)
    }
}