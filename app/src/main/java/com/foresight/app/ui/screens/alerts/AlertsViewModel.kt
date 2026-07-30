package com.foresight.app.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresight.app.data.local.relations.ItemWithCategory
import com.foresight.app.repository.ItemRepository
import com.foresight.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertsUiState(
    val expiredItems: List<ItemWithCategory> = emptyList(),
    val expiringItems: List<ItemWithCategory> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        // Expired items
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            itemRepository.getExpiredItems(now).collect { items ->
                _uiState.update { it.copy(expiredItems = items) }
            }
        }

        // Expiring within 7 days
        viewModelScope.launch {
            val now = DateUtils.startOfDay(System.currentTimeMillis())
            val weekFromNow = DateUtils.daysFromNow(7)
            itemRepository.getItemsExpiringBetween(now, weekFromNow).collect { items ->
                _uiState.update { it.copy(expiringItems = items, isLoading = false) }
            }
        }
    }
}
