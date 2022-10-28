package ru.petr.songapp.data.models.songData

import androidx.room.*

@Entity(
    tableName = "CollectionSections",
    foreignKeys = [
        ForeignKey(
            entity = SongCollectionDBModel::class,
            parentColumns = ["Id"],
            childColumns = ["CollectionId"]
        ),
    ],
    indices = [
        Index("CollectionId"),
    ]
)
data class CollectionSection(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @ColumnInfo(name = "CollectionId") val collectionId: Int,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "NumberInCollection") val numberInCollection: Int
)