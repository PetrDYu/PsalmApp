package ru.petr.psalmapp.data.models.psalm_data.utils

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import ru.petr.psalmapp.data.models.PsalmAppDB
import ru.petr.psalmapp.data.models.psalm_data.CollectionSection
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection

const val INFO_FILE_EXT = "info"
const val COLLECTIONS_FOLDER = "collections"
const val COLLECTION_INFO_FILE = "collection.$INFO_FILE_EXT"
const val SECTION_INFO_FILE = "section.$INFO_FILE_EXT"

const val LOG_TAG = "create_db_utils"

suspend fun populateDBFromAssets(appContext: Context, database: PsalmAppDB) {
    CoroutineScope(Dispatchers.IO).launch {
        appContext.assets.list("$COLLECTIONS_FOLDER/")?.forEach { collection ->
            val shortCollectionName: String = getShortCollectionName(appContext, collection)
            val collectionId = database.PsalmCollectionDao().insert(PsalmCollection(0, collection, shortCollectionName)).toInt()
            appContext.assets.list("$COLLECTIONS_FOLDER/$collection/")?.forEach { sectionForlder ->
                if (!sectionForlder.endsWith(".$INFO_FILE_EXT")) {
                    val sectionName: String = getSectionName(appContext, collection, sectionForlder)
                    val sectionId = database.CollectionSectionDao().insert(CollectionSection(0, collectionId, sectionName, sectionForlder.toInt())).toInt()
                    appContext.assets.list("$COLLECTIONS_FOLDER/$collection/$sectionForlder/")?.forEach { psalmFile ->
                        if (!psalmFile.endsWith(".$INFO_FILE_EXT")) {
                            val factory = XmlPullParserFactory.newInstance()
                            factory.isNamespaceAware = true
                            val parser: XmlPullParser = factory.newPullParser()
                            val newPsalm = parsePsalmFile(appContext, parser, psalmFile, collectionId, collection, sectionId, sectionForlder)
                            database.PsalmDao().insert(newPsalm)
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

fun parsePsalmFile(appContext: Context, parser: XmlPullParser, psalmFile: String, collectionId: Int, collectionName: String, sectionId: Int, sectionFolder: String): Psalm{
    val file = appContext.assets.open("$COLLECTIONS_FOLDER/$collectionName/$sectionFolder/$psalmFile")
    Log.d(LOG_TAG, "parsing file: $psalmFile")
    parser.setInput(file, "UTF-8")
    var numberInCollection = 0
    var name = ""
    var isCanon = false
    var textAuthors = ""
    var textRusAuthors = ""
    var musicComposers = ""
    var additionalInfo = ""
    while (parser.eventType != XmlPullParser.END_DOCUMENT) {
        if (parser.eventType == XmlPullParser.START_TAG && parser.name == "psalm") {
            for (attrI in 0 until parser.attributeCount) {
                when (parser.getAttributeName(attrI)) {
                    "number" -> { numberInCollection = parser.getAttributeValue(attrI).toInt() }
                    "name" -> { name = parser.getAttributeValue(attrI) }
                    "canon" -> { isCanon = parser.getAttributeValue(attrI).toBoolean() }
                    "text" -> { textAuthors = parser.getAttributeValue(attrI) }
                    "textRus" -> { textRusAuthors = parser.getAttributeValue(attrI) }
                    "music" -> { musicComposers = parser.getAttributeValue(attrI) }
                    "additionalInfo" -> { additionalInfo = parser.getAttributeValue(attrI) }
                }
            }
        }
        parser.next()
    }
    return Psalm(
        0,
        collectionId,
        sectionId,
        name,
        numberInCollection,
        isCanon,
        textAuthors,
        textRusAuthors,
        musicComposers,
        additionalInfo,
        appContext.assets
            .open("$COLLECTIONS_FOLDER/$collectionName/$sectionFolder/$psalmFile")
            .bufferedReader().readText()
    )
}