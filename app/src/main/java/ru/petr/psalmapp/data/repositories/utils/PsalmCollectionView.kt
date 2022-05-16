package ru.petr.psalmapp.data.repositories.utils

import kotlinx.coroutines.flow.Flow
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.PsalmCollection
import ru.petr.psalmapp.data.models.psalm_data.dao.ShortPsalm

data class PsalmCollectionView(val psalmCollection: PsalmCollection, val psalms: Flow<List<ShortPsalm>>)
