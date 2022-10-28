package ru.petr.songapp.ui.screens.songScreens.models.parsing

import org.junit.Assert
import org.junit.Test
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.songData.SongData
import ru.petr.songapp.data.models.songData.SongWithCollectionFromDB
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import java.io.File
import java.util.*

class SongBuilderTest {

    @Test
    fun `when gettingSong with correct input shuld return correct Song object`(){
        val songWithCollectionFromDB = SongWithCollectionFromDB(
            1,
            1,
            SongCollectionDBModel(1, "test", "test"),
            SongData(
                "test",
                1,
                false,
                "",
                "",
                "",
                "",
                false,
            ),
            File("C:\\Users\\petry\\YandexDisk\\Rabota\\MyProjects\\PsalmApp\\app\\src\\main\\assets\\collections\\Будем петь и славить\\7\\201 Просвети меня, Боже, чтоб ясен был путь,.xml").readText(),
        )

        val date1 = Date()
        println(date1.time)
        var songAct = SongBuilder.getSong(songWithCollectionFromDB, SongCollection(false, "test", "test"))
        for (i in 0..100){
            songAct = SongBuilder.getSong(songWithCollectionFromDB, SongCollection(false, "test$i", "test$i"))
        }
        val date2 = Date()
        println(date2.time)
        Assert.assertEquals(1, songAct.mSongParts.size)
        Assert.assertFalse(songAct.mIsFixed)
    }
}