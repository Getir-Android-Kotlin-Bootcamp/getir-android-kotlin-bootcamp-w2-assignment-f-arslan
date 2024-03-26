package com.getir.patika.foodmap

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.getir.patika.foodmap.data.LocationRepository
import com.getir.patika.foodmap.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val preferencesRepository: PreferencesRepository
) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val _autoCompleteResults = MutableStateFlow(emptyList<AutoCompleteResult>())
    val autoCompleteResults = _autoCompleteResults.asStateFlow()

    val dialogState: Boolean
        get() = uiState.value.dialogState

    fun onQueryTextChange(query: String?) = viewModelScope.launch {
        _uiState.update { it.copy(query = query ?: "") }
        query?.let { searchPlaces(it) }
    }

    init {
        initializePreferences()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() = launchCatching {
        _uiState.update { it.copy(locationResult = LocationResult.Loading) }

        if (uiState.value.currentLocationResult is LocationResult.Success) {
            delay(100L)
            _uiState.update { it.copy(locationResult = uiState.value.currentLocationResult) }
            return@launchCatching
        }


        val locationResult = locationRepository.getCurrentLocation()
        _uiState.update {
            it.copy(
                locationResult = locationResult,
                currentLocationResult = locationResult
            )
        }
    }

    private var searchJob: Job? = null
    private fun searchPlaces(query: String) {
        searchJob?.cancel()
        _autoCompleteResults.update { emptyList() }

        searchJob = launchCatching {
            val autoCompleteResults = locationRepository.searchPlaces(query)
            _autoCompleteResults.update { autoCompleteResults }
        }
    }

    fun getPlaceDetails(autoCompleteResult: AutoCompleteResult) = launchCatching {
        val locationResult = locationRepository.getPlaceDetails(autoCompleteResult.placeId)
        _autoCompleteResults.update { emptyList() }
        _uiState.update { it.copy(locationResult = locationResult, query = "") }
    }

    private fun initializePreferences() = launchCatching {
        val dialogState = preferencesRepository.getGreetingDialogState()
        _uiState.update {
            it.copy(dialogState = dialogState)
        }
    }

    fun setDialogState() = launchCatching {
        preferencesRepository.setGreetingDialogState()
        initializePreferences()
    }
}
