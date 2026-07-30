package com.foresight.app.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresight.app.data.local.entity.Item
import com.foresight.app.data.local.relations.ItemWithCategory
import com.foresight.app.repository.ItemRepository
import com.foresight.app.util.DateUtils
import com.foresight.app.util.ExpiryUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemDetailUiState(
    val itemWithCategory: ItemWithCategory? = null,
    val isLoading: Boolean = true,
    val daysUntilExpiry: Long = 0,
    val severity: ExpiryUtils.ExpirySeverity = ExpiryUtils.ExpirySeverity.SAFE,
    val isDeleted: Boolean = false
)

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: Long = savedStateHandle.get<Long>("itemId") ?: -1L

    private val _uiState = MutableStateFlow(ItemDetailUiState())
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            itemRepository.getItemWithCategory(itemId).collect { itemWithCategory ->
                if (itemWithCategory != null) {
                    val daysLeft = DateUtils.daysUntil(itemWithCategory.item.expiryDate)
                    _uiState.update {
                        it.copy(
                            itemWithCategory = itemWithCategory,
                            daysUntilExpiry = daysLeft,
                            severity = ExpiryUtils.getExpirySeverity(daysLeft),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun markAsDiscarded() {
        viewModelScope.launch {
            itemRepository.updateStatus(itemId, 2) // DISCARDED
        }
    }

    fun markAsReplaced() {
        viewModelScope.launch {
            itemRepository.updateStatus(itemId, 3) // REPLACED

            // If recurring, create new instance
            val item = itemRepository.getItemByIdOnce(itemId)
            if (item != null && item.isRecurring && item.recurrenceDays != null) {
                val newItem = item.copy(
                    id = 0,
                    expiryDate = System.currentTimeMillis() + (item.recurrenceDays * 24 * 60 * 60 * 1000L),
                    status = 0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                itemRepository.insert(newItem)
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            itemRepository.deleteById(itemId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}
