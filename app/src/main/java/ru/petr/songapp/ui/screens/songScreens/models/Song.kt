package ru.petr.songapp.ui.screens.songScreens.models

import ru.petr.songapp.ui.screens.songScreens.models.songParts.SongPart
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer
import ru.petr.songapp.ui.screens.songScreens.models.utils.*
import java.util.Collections.max
import kotlin.reflect.full.isSubclassOf

//TODO в зависимости от контекста должен выдавать по-разному отформатированный текст
// (сплошным текстом или по частям для редактирования)
class Song (
    val mSongParts: List<SongPart>,
    val mNumberInCollection: SongNumberInCollection,
    val mIsFixed: Boolean,
    val mLayerStack: LayerStack
) {

    init {

    }

    fun clone(
        numberInCollection: SongNumberInCollection,
        isFixed: Boolean,
        layerStack: LayerStack = mLayerStack,
    ): Song {
        return Song(mSongParts, numberInCollection, isFixed, layerStack)
    }

    class LayerStack {
        private val addingLayers = mutableListOf<StackedLayer<ChunkLayer.AddingLayer>>()
        private val wrappingLayers = mutableListOf<StackedLayer<ChunkLayer.WrappingLayer>>()

        fun addLayer(layer: ChunkLayer): AddLayerResults {
            val result: AddLayerResults
            if (layer::class.isSubclassOf(ChunkLayer.AddingLayer::class)) {
                result = addLayerInList(layer, addingLayers)
            } else if (layer::class.isSubclassOf(ChunkLayer.WrappingLayer::class)) {
                result = addLayerInList(layer, wrappingLayers)
            } else {
                throw IllegalArgumentException("layer is not subclass of AddingLayer and WrappingLayer")
            }
            return result
        }

        private fun <T: ChunkLayer> addLayerInList(
            layer: ChunkLayer,
            layersList: MutableList<StackedLayer<T>>
        ): AddLayerResults {
            var result = AddLayerResults.SUCCESSFULLY_ADDED
            layersList.forEach { stackedLayer ->
                if ((stackedLayer.layer::class == layer::class) && (stackedLayer.layer.layerId == layer.layerId)) {
                    //throw IllegalArgumentException("Layer ${layer::class.simpleName} with id=${layer.layerId} is already in stack")
                    result = AddLayerResults.ALREADY_EXISTS
                }
            }
            if (result != AddLayerResults.ALREADY_EXISTS) {
                var newPosition = 0
                if (layersList.isNotEmpty()){
                    newPosition = max(layersList.map { it.layerPosition }) + 1
                }

                layersList.add(
                    StackedLayer(
                        layer = layer as T,
                        layerPosition = newPosition
                    )
                )
            }

            return result
        }

        enum class AddLayerResults {
            SUCCESSFULLY_ADDED,
            ALREADY_EXISTS,
            ERROR_WHILE_ADDING
        }

        data class StackedLayer<T: ChunkLayer>(val layer: T,
                                val layerPosition: Int = 0,
                                val layerState: LayerStates = LayerStates.LAYER_ON) {
            init {
                if (layerPosition < 0) {
                    throw IllegalArgumentException("LayerPosition should be not negative")
                }
            }
        }

        enum class LayerStates {
            LAYER_ON,
            LAYER_OFF
        }
        /* TODO:
        *   1. Надо создать внутренний класс слоя для стека, который будет хранить место в стеке
        *       и состояние слоя (включен/выключен)
        *   Под вопросом:
        *   (2. Вероятно функции get надо разделить на getAll...Layers, getOn...Layers, и т.д.)
        *   (3. Из этих функций должны возвращаться отсортированные по порядку слоёв массивы
        *       чистых ChunkLayer)
        *   Это точно:
        *   4. стек слоёв нужно передавать в LineBuilder, чтобы там мог сформироваться полный стек
        * */
    }
}