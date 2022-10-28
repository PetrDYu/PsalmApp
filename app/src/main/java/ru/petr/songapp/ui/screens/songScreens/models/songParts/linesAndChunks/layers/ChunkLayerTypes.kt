package ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers

import ru.petr.songapp.ui.screens.songScreens.models.parsing.TagAndAttrNames

enum class ChunkLayerTypes {
    ContinuousDataLayer,
    MarkDataLayer;

    companion object {
        private val continuousDataLayers = listOf<String>(
            TagAndAttrNames.REPEAT_TAG._name,
        )
        private val markDataLayers = listOf<String>(

        )

        fun getLayerTypeByName(layerTagName: String): ChunkLayerTypes {
            return when(layerTagName) {
                in continuousDataLayers -> {
                    ContinuousDataLayer
                }
                in markDataLayers -> {
                    MarkDataLayer
                }
                else -> {
                    throw IllegalArgumentException("Layer with name $layerTagName doesn't exist")
                }
            }
        }
    }

}