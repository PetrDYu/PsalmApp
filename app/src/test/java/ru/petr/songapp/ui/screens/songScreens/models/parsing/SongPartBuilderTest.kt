package ru.petr.songapp.ui.screens.songScreens.models.parsing

import android.nfc.Tag
import org.junit.Assert.*
import org.junit.Test
import ru.petr.songapp.ui.screens.songScreens.models.Song

class SongPartBuilderTest {

    @Test
    fun buildVerseTest() {
        val stack = Song.LayerStack()
        val xmlString = "<verse number=\"1\">\n" +
                "        <string>\n" +
                "            <repeat id=\"0\" rep_rate=\"-1\" is_opening=\"true\" />\n" +
                "            <plain>Радуйтесь всегда, молитесь непрестанно,</plain>\n" +
                "        </string>\n" +
                "        <string>\n" +
                "            <plain>За все благодарите.</plain>\n" +
                "            <repeat id=\"0\" rep_rate=\"3\" is_opening=\"false\" />\n" +
                "        </string>\n" +
                "    </verse>"
        val attributes = mapOf(TagAndAttrNames.NUMBER_ATTR._name to "1")

        val builder = SongPartBuilder.getPartBuilder(TagAndAttrNames.VERSE_TAG._name)
        val part = builder?.let { it(stack, xmlString, attributes) }
        val a = 2
    }
}