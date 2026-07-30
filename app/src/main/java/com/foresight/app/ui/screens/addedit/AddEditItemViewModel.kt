package com.foresight.app.ui.screens.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresight.app.data.local.entity.Alert
import com.foresight.app.data.local.entity.Category
import com.foresight.app.data.local.entity.Item
import com.foresight.app.repository.CategoryRepository
import com.foresight.app.repository.ItemRepository
import com.foresight.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val name: String = "",
    val selectedCategoryId: Long? = null,
    val expiryDate: Long = DateUtils.daysFromNow(30),
    val notes: String = "",
    val photoUri: String? = null,
    val isRecurring: Boolean = false,
    val recurrenceDays: Int = 30,
    val alertDays: List<Int> = listOf(7, 3),
    val categories: List<Category> = emptyList(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddEditItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: Long = savedStateHandle.get<Long>("itemId")?.takeIf { it > 0 } ?: -1L

    private val _uiState = MutableStateFlow(AddEditUiState(isEditing = itemId > 0))
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
        if (itemId > 0) {
            loadItem()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            itemRepository.getItemByIdOnce(itemId)?.let { item ->
                _uiState.update {
                    it.copy(
                        name = item.name,
                        selectedCategoryId = item.categoryId,
                        expiryDate = item.expiryDate,
                        notes = item.notes,
                        photoUri = item.photoUri,
                        isRecurring = item.isRecurring,
                        recurrenceDays = item.recurrenceDays ?: 30
                    )
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateCategory(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun updateExpiryDate(date: Long) {
        _uiState.update { it.copy(expiryDate = date) }
    }

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun updateRecurring(isRecurring: Boolean) {
        _uiState.update { it.copy(isRecurring = isRecurring) }
    }

    fun updateRecurrenceDays(days: Int) {
        _uiState.update { it.copy(recurrenceDays = days) }
    }

    fun updateAlertDays(days: List<Int>) {
        _uiState.update { it.copy(alertDays = days) }
    }

    fun save() {
        val state = _uiState.value

        // Validate
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "Name is required") }
            return
        }
        if (state.selectedCategoryId == null) {
            _uiState.update { it.copy(error = "Select a category") }
            return
        }

        _uiState.update { it.copy(isSaving = true, error = null) }

        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                val item = if (state.isEditing) {
                    val existing = itemRepository.getItemByIdOnce(itemId)
                    existing?.copy(
                        name = state.name,
                        categoryId = state.selectedCategoryId,
                        expiryDate = state.expiryDate,
                        notes = state.notes,
                        photoUri = state.photoUri,
                        isRecurring = state.isRecurring,
                        recurrenceDays = if (state.isRecurring) state.recurrenceDays else null,
                        updatedAt = now
                    )
                } else {
                    Item(
                        name = state.name,
                        categoryId = state.selectedCategoryId,
                        expiryDate = state.expiryDate,
                        notes = state.notes,
                        photoUri = state.photoUri,
                        isRecurring = state.isRecurring,
                        recurrenceDays = if (state.isRecurring) state.recurrenceDays else null,
                        createdAt = now,
                        updatedAt = now
                    )
                }

                if (item != null) {
                    val insertedId = if (state.isEditing) {
                        itemRepository.update(item)
                        item.id
                    } else {
                        itemRepository.insert(item)
                    }

                    // Save alert preferences
                    state.alertDays.forEach { days ->
                        itemRepository.insertAlert(
                            Alert(
                                itemId = insertedId,
                                alertDays = days
                            )
                        )
                    }
                }

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }
}
