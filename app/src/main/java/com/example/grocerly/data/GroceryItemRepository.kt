package com.example.grocerly.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class GroceryItemRepository @Inject constructor(
    private val dao: GroceryItemDao
) {
    fun getAllGroceryItems(): Flow<List<GroceryItem>> = dao.getAll()

    suspend fun insertOrUpdateGroceryItem(vararg items: GroceryItem) {
        return dao.insertOrUpdate(*items)
    }

    suspend fun deleteGroceryItem(item: GroceryItem) {
        dao.delete(item)
    }
}