package com.example.grocerly

import com.example.grocerly.data.GroceryItem

fun createGroceryItem(
    id: Long = 0L,
    name: String = "Item",
    quantity: Int = 1,
    positionIndex: Int = 0,
    checkedOff: Boolean = false,
): GroceryItem {
    return GroceryItem(
        id = id,
        name = name,
        quantity = quantity,
        positionIndex = positionIndex,
        checkedOff = checkedOff,
    )
}
