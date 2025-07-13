package com.example.grocerly

sealed class GroceryListEvent {

    data class ReorderGroceryItem(val from: Int, val to: Int) : GroceryListEvent()

    data class UpdateNewGroceryItemName(val name: String) : GroceryListEvent()

    data class UpdateNewGroceryItemQuantity(val quantity: Int) : GroceryListEvent()

    object ShowAddDialog : GroceryListEvent()

    object HideAddDialog : GroceryListEvent()

    object AddGroceryItem : GroceryListEvent()
}
