package io.lowapple.app.android.petking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lowapple.app.android.petking.domain.models.PetKingLocation
import io.lowapple.app.android.petking.domain.repositories.PetKingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetKingViewModel @Inject constructor(
    private val repository: PetKingRepository
) : ViewModel() {
    private var job: Job? = null
    private val _locations: MutableLiveData<List<PetKingLocation>> = MutableLiveData(arrayListOf())
    val locations: LiveData<List<PetKingLocation>> = _locations

    fun tracking(uuid: String) {
        job?.cancel()
        job = null
        job = viewModelScope.launch {
            repository.getLocations(uuid).collect {
                _locations.postValue(it)
            }
        }
    }
}