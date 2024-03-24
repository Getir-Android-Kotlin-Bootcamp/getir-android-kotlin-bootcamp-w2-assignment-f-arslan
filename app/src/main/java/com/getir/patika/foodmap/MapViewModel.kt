package com.getir.patika.foodmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MapUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val query: String = ""
)

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    fun onQueryTextChange(query: String?) = viewModelScope.launch {
        _uiState.update { it.copy(query = query ?: "") }
    }
}
