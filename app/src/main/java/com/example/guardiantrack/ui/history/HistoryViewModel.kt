package com.example.guardiantrack.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guardiantrack.data.repository.IncidentRepositoryImpl
import com.example.guardiantrack.domain.model.Incident
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val incidents: List<Incident> = emptyList(),
    val isLoading: Boolean = false,
    val exportMessage: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val incidentRepo: IncidentRepositoryImpl
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> =
        incidentRepo.getAllIncidents().map { list ->
            HistoryUiState(incidents = list, isLoading = false)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HistoryUiState(isLoading = true)
        )

    fun deleteIncident(id: Long) {
        viewModelScope.launch { incidentRepo.deleteIncident(id) }
    }
}