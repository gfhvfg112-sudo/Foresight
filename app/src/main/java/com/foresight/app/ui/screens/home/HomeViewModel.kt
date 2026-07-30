package com.foresight.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresight.app.data.local.entity.Item
import com.foresight.app.data.local.relations.ItemWithCategory
import com.foresight.app.repository.ItemRepository
import com.foresight.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val totalActive: Int = 0,
    val expiringThisWeek: List<ItemWithCategory> = emptyList(),
    val expired: List<ItemWithCategory> = emptyList(),
    val recentItems: List<ItemWithCategory> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        // Total active count
        viewModelScope.launch {
            itemRepository.getActiveItemCount().collect { count ->
                _uiState.update { it.copy(totalActive = count) }
            }
        }

        // Expiring this week (next 7 days)
        viewModelScope.launch {
            val now = DateUtils.startOfDay(System.currentTimeMillis())
            val weekFromNow = DateUtils.daysFromNow(7)

            itemRepository.getItemsExpiringBetween(now, weekFromNow).collect { items ->
                _uiState.update { it.copy(expiringThisWeek = items) }
            }
        }

        // Expired items
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            itemRepository.getExpiredItems(now).collect { items ->
                _uiState.update { it.copy(expired = items) }
            }
        }

        // All active items
        viewModelScope.launch {
            itemRepository.getActiveItems().collect { items ->
                _uiState.update {
                    it.copy(
                        recentItems = items,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemRepository.delete(item)
        }
    }

    fun updateItemStatus(itemId: Long, status: Int) {
        viewModelScope.launch {
            itemRepository.updateStatus(itemId, status)
        }
    }
}
