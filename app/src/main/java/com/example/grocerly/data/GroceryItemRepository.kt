package com.example.grocerly.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroceryItemRepository @Inject constructor(
    private val dao: GroceryItemDao
) {
    fun getAllGroceryItems(): Flow<List<GroceryItem>> = dao.getAllAsFlow()

    suspend fun insertOrUpdateGroceryItem(vararg items: GroceryItem) {
        return dao.insertOrUpdate(*items)
    }

    suspend fun deleteGroceryItem(item: GroceryItem) {
        val allItems = dao.getAll()
        val isLastItem = item.positionIndex == allItems.lastIndex
        if (isLastItem) {
            dao.delete(item)
            return
        }

        val allFollowingItems = allItems
            .sortedBy { it.positionIndex }
            .drop(item.positionIndex + 1)
            .toMutableList()

        for (i in 0..allFollowingItems.lastIndex) {
            val currentItem = allFollowingItems[i]
            allFollowingItems[i] = currentItem.copy(positionIndex = currentItem.positionIndex - 1)
        }
        dao.insertOrUpdate(*allFollowingItems.toTypedArray())
        dao.delete(item)
    }
}