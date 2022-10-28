package ru.petr.songapp

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import ru.petr.songapp.ui.screens.songScreens.models.songParts.linesAndChunks.layers.ChordLayer
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val a = 2
        var chordLayer = ChordLayer(0)
        val date1 = Date()
        println(date1.time)
        var result = false
        for (i in 1..1000000) {
            result = chordLayer.isSameWithTagNameLayerChunkIdAndLayerId("chord", i, 0)
            chordLayer = ChordLayer(i)
        }
//        val res = chordLayer::class.simpleName
        val date2 = Date()
        println(date2.time)
//        assertTrue(result)
    }
}