package com.example.grocerly

import com.example.grocerly.data.GroceryItem

data class GroceryListState(
    val groceryItems: List<GroceryItem> = emptyList(),
    val showAddGroceryItemDialog: Boolean = false,
    val newGroceryItemName: String = "",
    val newGroceryItemQuantity: Int = 1,
)
