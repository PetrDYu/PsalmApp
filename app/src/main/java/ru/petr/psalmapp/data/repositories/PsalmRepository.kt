package ru.petr.psalmapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.models.psalm_data.dao.PsalmDao

class PsalmRepository(private val psalmDao: PsalmDao) {

    fun getPsalmById(id: Int): LiveData<Psalm> {
        return psalmDao.getPsalmById(id).asLiveData()
    }
}