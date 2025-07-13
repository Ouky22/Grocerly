package com.example.grocerly

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.grocerly.data.GroceryItem
import java.util.Collections
import kotlin.random.Random

@HiltViewModel
class GroceryListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(
        GroceryListState(
            groceryItems = listOf(
                GroceryItem(id = 1L, name = "Apples", quantity = 5, positionIndex = 0),
                GroceryItem(id = 2L, name = "Bananas", quantity = 7, positionIndex = 1),
                GroceryItem(id = 3L, name = "Carrots", quantity = 3, positionIndex = 2),
                GroceryItem(id = 4L, name = "Bread", quantity = 2, positionIndex = 3),
                GroceryItem(id = 5L, name = "Milk", quantity = 1, positionIndex = 4),
                GroceryItem(id = 6L, name = "Eggs", quantity = 12, positionIndex = 5),
                GroceryItem(id = 7L, name = "Chicken Breast", quantity = 4, positionIndex = 6),
                GroceryItem(id = 8L, name = "Rice", quantity = 2, positionIndex = 7),
                GroceryItem(id = 9L, name = "Tomatoes", quantity = 6, positionIndex = 8),
                GroceryItem(id = 10L, name = "Cheese", quantity = 1, positionIndex = 9),
            )
        )
    )
    val state: StateFlow<GroceryListState> = _state.asStateFlow()

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
        val reorderedGroceryList = _state.value.groceryItems.toMutableList().apply {
            Collections.swap(this, from, to)
            this[from] = this[from].copy(positionIndex = from)
            this[to] = this[to].copy(positionIndex = to)
        }
        _state.value = _state.value.copy(groceryItems = reorderedGroceryList)
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

        _state.value = _state.value.copy(
            groceryItems = _state.value.groceryItems + newGroceryItem,
            newGroceryItemName = "",
            newGroceryItemQuantity = 1,
            showAddGroceryItemDialog = false,
        )
    }
}
