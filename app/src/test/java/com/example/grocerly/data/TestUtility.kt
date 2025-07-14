package com.example.grocerly.data

fun createGroceryItem(
    id: Long = 0L,
    name: String = "Item",
    quantity: Int = 1,
    positionIndex: Int = 0
): GroceryItem {
    return GroceryItem(
        id = id,
        name = name,
        quantity = quantity,
        positionIndex = positionIndex
    )
}
