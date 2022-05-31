package ru.petr.psalmapp.ui.screens.psalmScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.repositories.PsalmRepository
import ru.petr.psalmapp.data.repositories.PsalmsByCollectionsRepository
import ru.petr.psalmapp.ui.screens.psalmCollectionScreen.PsalmListViewModel

class PsalmViewViewModel(private val psalmRepository: PsalmRepository) : ViewModel() {
    fun getPsalmById(id: Int): LiveData<Psalm> {
        return psalmRepository.getPsalmById(id)
    }
}

class PsalmViewViewModelFactory(private val repository: PsalmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PsalmViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PsalmViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
