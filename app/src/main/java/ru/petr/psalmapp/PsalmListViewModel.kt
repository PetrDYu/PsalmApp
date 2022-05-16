package ru.petr.psalmapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.petr.psalmapp.data.models.psalm_data.Psalm
import ru.petr.psalmapp.data.repositories.PsalmsByCollectionsRepository
import ru.petr.psalmapp.data.repositories.utils.PsalmCollectionView

class PsalmListViewModel(private val repository: PsalmsByCollectionsRepository) : ViewModel() {

    val psalmsByCollections: LiveData<List<PsalmCollectionView>> = repository.psalmsByCollections.asLiveData()

    fun insert(psalm: Psalm) = viewModelScope.launch { repository.insert(psalm) }
}

class PsalmListViewModelFactory(private val repository: PsalmsByCollectionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PsalmListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PsalmListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}