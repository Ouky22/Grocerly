package com.example.grocerly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerly.data.GroceryItem
import com.example.grocerly.data.GroceryItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

@HiltViewModel
class GroceryListViewModel @Inject constructor(
    private val groceryItemRepository: GroceryItemRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GroceryListState())

    val state: StateFlow<GroceryListState> = _state.asStateFlow()

    init {
        loadGroceryItems()
    }

    private fun loadGroceryItems() {
        viewModelScope.launch {
            groceryItemRepository.getAllGroceryItems().collect { groceryItems ->
                if (groceryItems.isEmpty()) {
                    listOf(
                        GroceryItem(name = "Apples", quantity = 5, positionIndex = 0),
                        GroceryItem(name = "Bananas", quantity = 7, positionIndex = 1),
                        GroceryItem(name = "Carrots", quantity = 3, positionIndex = 2),
                        GroceryItem(name = "Bread", quantity = 2, positionIndex = 3),
                        GroceryItem(name = "Milk", quantity = 1, positionIndex = 4),
                        GroceryItem(name = "Eggs", quantity = 12, positionIndex = 5),
                        GroceryItem(name = "Chicken Breast", quantity = 4, positionIndex = 6),
                        GroceryItem(name = "Rice", quantity = 2, positionIndex = 7),
                        GroceryItem(name = "Tomatoes", quantity = 6, positionIndex = 8),
                        GroceryItem(name = "Cheese", quantity = 1, positionIndex = 9),
                    ).forEach { item -> groceryItemRepository.insertOrUpdateGroceryItem(item) }
                }

                _state.value = _state.value.copy(
                    groceryItems = groceryItems.sortedBy { it.positionIndex }
                )
            }
        }
    }

    fun onEvent(event: GroceryListEvent) {
        when (event) {
            is GroceryListEvent.ReorderGroceryItem
                -> reorderGroceryItem(event.from, event.to)

            is GroceryListEvent.ShowAddDialog
                -> showAddDialog()

            is GroceryListEvent.HideAddDialog
                -> hideAddDialog()

            is GroceryListEvent.UpdateNewGroceryItemName
                -> updateNewItemName(event.name)

            is GroceryListEvent.UpdateNewGroceryItemQuantity
                -> updateNewItemQuantity(event.quantity)

            is GroceryListEvent.AddGroceryItem
                -> addGroceryToCurrentList()
        }
    }

    private fun reorderGroceryItem(from: Int, to: Int) {
        val groceryItems = _state.value.groceryItems
        viewModelScope.launch {
            groceryItemRepository.insertOrUpdateGroceryItem(
                groceryItems[from].copy(positionIndex = to),
                groceryItems[to].copy(positionIndex = from),
            )
        }
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddGroceryItemDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddGroceryItemDialog = false)
    }

    private fun updateNewItemName(name: String) {
        _state.value = _state.value.copy(newGroceryItemName = name)
    }

    private fun updateNewItemQuantity(quantity: Int) {
        _state.value = _state.value.copy(newGroceryItemQuantity = quantity)
    }

    private fun addGroceryToCurrentList() {
        val newGroceryItemName = _state.value.newGroceryItemName
        val newGroceryItemQuantity = _state.value.newGroceryItemQuantity

        if (newGroceryItemName.isBlank() || newGroceryItemQuantity !in 1..10) {
            _state.value = _state.value.copy(showAddGroceryItemDialog = false)
            return
        }

        val newGroceryItem = GroceryItem(
            id = Random.nextLong(),
            name = newGroceryItemName,
            quantity = newGroceryItemQuantity,
            positionIndex = _state.value.groceryItems.size,
        )

        viewModelScope.launch {
            groceryItemRepository.insertOrUpdateGroceryItem(newGroceryItem)
            _state.value = _state.value.copy(
                newGroceryItemName = "",
                newGroceryItemQuantity = 1,
                showAddGroceryItemDialog = false,
            )
        }
    }
}
