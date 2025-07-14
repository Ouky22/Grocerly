package com.example.grocerly

import com.example.grocerly.data.GroceryItem

sealed class GroceryListEvent {

    data class ReorderGroceryItem(val from: Int, val to: Int) : GroceryListEvent()

    data class UpdateNewGroceryItemName(val name: String) : GroceryListEvent()

    data class UpdateNewGroceryItemQuantity(val quantity: Int) : GroceryListEvent()

    object ShowAddDialog : GroceryListEvent()

    object HideAddDialog : GroceryListEvent()

    object AddGroceryItem : GroceryListEvent()

    data class DeleteGroceryItem(val groceryItem: GroceryItem) : GroceryListEvent()

    data class CheckOffGroceryItem(val groceryItem: GroceryItem) : GroceryListEvent()

    object ToggleEditMode : GroceryListEvent()
}
