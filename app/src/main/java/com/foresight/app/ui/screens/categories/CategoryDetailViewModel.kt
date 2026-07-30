package com.foresight.app.ui.screens.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresight.app.data.local.entity.Category
import com.foresight.app.data.local.relations.ItemWithCategory
import com.foresight.app.repository.CategoryRepository
import com.foresight.app.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryDetailUiState(
    val category: Category? = null,
    val items: List<ItemWithCategory> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Long = savedStateHandle.get<Long>("categoryId") ?: -1L

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    init {
        loadCategory()
        loadItems()
    }

    private fun loadCategory() {
        viewModelScope.launch {
            categoryRepository.getCategoryById(categoryId).collect { category ->
                _uiState.update { it.copy(category = category) }
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            itemRepository.getItemsByCategory(categoryId).collect { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }
}
