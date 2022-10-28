package ru.petr.songapp.ui.screens.songScreens.models.parsing

import org.junit.Assert.*
import org.junit.Test
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChunkLayer

class LineBuilderTest{

    @Test
    fun buildLineTest() {
        var xmlSt = "        <string>\n" +
                    "            <repeat id=\"0\" rep_rate=\"-1\" is_opening=\"true\" />\n" +
                    "            <plain>Радуйтесь всегда, молитесь непрестанно,</plain>\n" +
                    "        </string>"
        val curLayers = SongPartBuilder.CurrentLayersHolder()
        val stack = Song.LayerStack()
        val line1 = LineBuilder.buildLine(xmlSt, curLayers, stack, {})
        xmlSt = "        <string>\n" +
                "            <plain>За все благодарите.</plain>\n" +
                "            <repeat id=\"0\" rep_rate=\"3\" is_opening=\"false\" />\n" +
                "        </string>"
        val line2 = LineBuilder.buildLine(xmlSt, curLayers, stack, {})
        val a = 2
    }
}