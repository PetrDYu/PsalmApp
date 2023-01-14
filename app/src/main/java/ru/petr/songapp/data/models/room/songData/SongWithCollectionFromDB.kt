package ru.petr.songapp.data.models.room.songData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class SongWithCollectionFromDB (

    @ColumnInfo(name = "Id")
    val id: Int,

    @ColumnInfo(name = "CollectionId")
    val collectionId: Int,

    @Relation(parentColumn = "CollectionId", entityColumn = "Id")
    val collection: SongCollectionDBModel,

    @Embedded
    val songData: SongData,

    @ColumnInfo(name = "Body")
    val body: String,
)