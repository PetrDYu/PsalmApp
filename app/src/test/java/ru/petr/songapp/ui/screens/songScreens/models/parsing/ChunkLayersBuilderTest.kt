package ru.petr.songapp.ui.screens.songScreens.models.parsing

import android.nfc.Tag
import org.junit.Assert.*
import org.junit.Test
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer

class ChunkLayersBuilderTest {
    @Test
    fun modifyLayerListTest() {
        val stack = Song.LayerStack()
        val curLayers = SongPartBuilder.CurrentLayersHolder()
        val attributes = mutableMapOf(
            TagAndAttrNames.ID_ATTR._name to "0",
            TagAndAttrNames.LAYER_ID_ATTR._name to "0",
            TagAndAttrNames.REP_RATE_ATTR._name to "-1",
            TagAndAttrNames.IS_OPENING_ATTR._name to "true"
        )

        ChunkLayersBuilder.modifyLayerList(stack, curLayers, "repeat", attributes, {}, {})
        attributes[TagAndAttrNames.IS_OPENING_ATTR._name] = "false"
        attributes[TagAndAttrNames.REP_RATE_ATTR._name] = "2"
        ChunkLayersBuilder.modifyLayerList(stack, curLayers, "repeat", attributes, {}, {})
        val a = 2
    }
}