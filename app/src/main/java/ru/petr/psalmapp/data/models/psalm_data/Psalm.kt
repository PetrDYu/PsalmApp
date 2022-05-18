package ru.petr.psalmapp.data.models.psalm_data

import androidx.room.*

@Entity(
    tableName = "Psalms",
    foreignKeys = [
        ForeignKey(
            entity = PsalmCollection::class,
            parentColumns = ["Id"],
            childColumns = ["CollectionId"]
        ),
        ForeignKey(
            entity = CollectionSection::class,
            parentColumns = ["Id"],
            childColumns = ["SectionId"]
        )
    ],
    indices = [
        Index("CollectionId"),
        Index("SectionId"),
        Index("Name"),
        Index("CollectionId", "NumberInCollection")
    ]
)
data class Psalm(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @ColumnInfo(name = "CollectionId") val collectionId: Int,
    @ColumnInfo(name = "SectionId") val sectionId: Int,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "NumberInCollection") val numberInCollection: Int,
    @ColumnInfo(name = "IsCanon") val isCanon: Boolean,
    @ColumnInfo(name = "TextAuthors") val textAuthors: String,
    @ColumnInfo(name = "RusTextAuthors") val rusTextAuthors: String,
    @ColumnInfo(name = "MusicComposers") val musicComposers: String,
    @ColumnInfo(name = "AdditionalInfo") val additionalInfo: String,
    @ColumnInfo(name = "Body") val body: String,
)