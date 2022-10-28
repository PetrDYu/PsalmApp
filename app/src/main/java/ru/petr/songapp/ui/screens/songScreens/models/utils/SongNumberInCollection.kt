package ru.petr.songapp.ui.screens.songScreens.models.utils

import ru.petr.songapp.ui.screens.songCollectionScreen.models.SongCollection

class SongNumberInCollection(number: Int, collection: SongCollection) {
    val mNumber: Int
    val mCollection: SongCollection

    init {
        mCollection = collection
        if (!mCollection.numberIsInCollection(number)) {

        }
        mNumber = number
    }
}