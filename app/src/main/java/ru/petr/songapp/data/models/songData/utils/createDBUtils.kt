package ru.petr.songapp.data.models.songData.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import ru.petr.songapp.data.models.SongAppDB
import ru.petr.songapp.data.models.songData.CollectionSection
import ru.petr.songapp.data.models.songData.SongDBModel
import ru.petr.songapp.data.models.songData.SongCollectionDBModel
import ru.petr.songapp.data.models.songData.SongData
import ru.petr.songapp.ui.screens.songScreens.models.parsing.TagAndAttrNames

const val INFO_FILE_EXT = "info"
const val COLLECTIONS_FOLDER = "collections"
const val COLLECTION_INFO_FILE = "collection.$INFO_FILE_EXT"
const val SECTION_INFO_FILE = "section.$INFO_FILE_EXT"

private const val LOG_TAG = "create_db_utils"

suspend fun populateDBFromAssets(appContext: Context, database: SongAppDB) {
    CoroutineScope(Dispatchers.IO).launch {
        appContext.assets.list("$COLLECTIONS_FOLDER/")?.forEach { collection ->
            val shortCollectionName: String = getShortCollectionName(appContext, collection)
            val collectionId = database.SongCollectionDao().insert(SongCollectionDBModel(0, collection, shortCollectionName)).toInt()
            appContext.assets.list("$COLLECTIONS_FOLDER/$collection/")?.forEach { sectionForlder ->
                if (!sectionForlder.endsWith(".$INFO_FILE_EXT")) {
                    val sectionName: String = getSectionName(appContext, collection, sectionForlder)
                    val sectionId = database.CollectionSectionDao().insert(CollectionSection(0, collectionId, sectionName, sectionForlder.toInt())).toInt()
                    appContext.assets.list("$COLLECTIONS_FOLDER/$collection/$sectionForlder/")?.forEach { songFile ->
                        if (!songFile.endsWith(".$INFO_FILE_EXT")) {
                            val factory = XmlPullParserFactory.newInstance()
                            factory.isNamespaceAware = true
                            val parser: XmlPullParser = factory.newPullParser()
                            val newSong = parseSongFile(appContext, parser, songFile, collectionId, collection, sectionId, sectionForlder)
                            database.SongDao().insert(newSong)
                        }
                    }
                }
            }
        }
    }

}

fun getShortCollectionName(appContext: Context, collectionName: String): String {
    return appContext.assets
        .open("$COLLECTIONS_FOLDER/$collectionName/$COLLECTION_INFO_FILE")
        .bufferedReader().readText()
}

fun getSectionName(appContext: Context, collectionName: String, sectionFolder: String): String {
    return appContext.assets
        .open("$COLLECTIONS_FOLDER/$collectionName/$sectionFolder/$SECTION_INFO_FILE")
        .bufferedReader().readText()
}

fun parseSongFile(appContext: Context, parser: XmlPullParser, songFile: String, collectionId: Int, collectionName: String, sectionId: Int, sectionFolder: String): SongDBModel{
    val file = appContext.assets.open("$COLLECTIONS_FOLDER/$collectionName/$sectionFolder/$songFile")
    Log.d(LOG_TAG, "parsing file: $songFile")
    parser.setInput(file, "UTF-8")
    var numberInCollection = 0
    var name = ""
    var isCanon = false
    var textAuthors = ""
    var textRusAuthors = ""
    var musicComposers = ""
    var additionalInfo = ""
    var plainText = ""
    while (parser.eventType != XmlPullParser.END_DOCUMENT) {
        if (parser.eventType == XmlPullParser.START_TAG && parser.name == TagAndAttrNames.SONG_TAG._name) {
            for (attrI in 0 until parser.attributeCount) {
                when (parser.getAttributeName(attrI)) {
                    TagAndAttrNames.NUMBER_ATTR._name -> { numberInCollection = parser.getAttributeValue(attrI).toInt() }
                    TagAndAttrNames.NAME_ATTR._name -> { name = parser.getAttributeValue(attrI) }
                    TagAndAttrNames.CANON_ATTR._name -> { isCanon = parser.getAttributeValue(attrI).toBoolean() }
                    TagAndAttrNames.TEXT_ATTR._name -> { textAuthors = parser.getAttributeValue(attrI) }
                    TagAndAttrNames.TEXT_RUS_ATTR._name -> { textRusAuthors = parser.getAttributeValue(attrI) }
                    TagAndAttrNames.MUSIC_ATTR._name -> { musicComposers = parser.getAttributeValue(attrI) }
                    TagAndAttrNames.ADDITIONAL_INFO_ATTR._name -> { additionalInfo = parser.getAttributeValue(attrI) }
                }
            }
        }
        if (parser.text != null) {
            plainText += parser.text.trim().lowercase() + (if (!plainText.endsWith(" ")) " " else "")
        }
        parser.next()
    }
    return SongDBModel(
        0,
        collectionId,
        sectionId,
        SongData(
            name,
            numberInCollection,
            isCanon,
            textAuthors,
            textRusAuthors,
            musicComposers,
            additionalInfo,
            true, // all songs from included files are fixed
        ),
        appContext.assets
            .open("$COLLECTIONS_FOLDER/$collectionName/$sectionFolder/$songFile")
            .bufferedReader().readText(),
        plainText,
    )
}