package ru.petr.songapp.ui.screens.songScreens.models.parsing

import org.xmlpull.v1.XmlPullParser
import ru.petr.songapp.data.models.songData.SongWithCollectionFromDB
import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection
import ru.petr.songapp.ui.screens.songScreens.models.Song
import ru.petr.songapp.ui.screens.songScreens.models.parsing.SongPartBuilder.getPartBuilder
import ru.petr.songapp.ui.screens.songScreens.models.songParts.SongPart
import ru.petr.songapp.ui.screens.songScreens.models.utils.*

object SongBuilder {

    fun getSong(
        song: SongWithCollectionFromDB,
        parentCollection: SongCollection
    ): Song {
        val numberInCollection = SongNumberInCollection(song.songData.numberInCollection, parentCollection)
        val songParts = mutableListOf<SongPart>()
        val layerStack = Song.LayerStack()

        val parser = instantiateNewParser(song.body.byteInputStream())
        var attributes: Map<String, String> = mapOf()
        val extractor = FromTagToTagExtractor()
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            getPartBuilder(parser.name)?.let { partBuilder ->
                if (parser.eventType == XmlPullParser.START_TAG) {
                    extractor.setStartPoint(parser.lineNumber, parser.columnNumber)
                    attributes = fetchAttributes(parser)
                } else if (parser.eventType == XmlPullParser.END_TAG) {
                    extractor.setEndPoint(parser.lineNumber, parser.columnNumber - 1)
                    songParts.add(
                        partBuilder(
                            layerStack,
                            extractor.extractPart(song.body),
                            attributes
                        )
                    )
                    extractor.clean()
                }
            }
            parser.next()
        }

        return Song(songParts, numberInCollection, song.songData.isFixed, layerStack)
    }
}