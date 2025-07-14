package com.example.grocerly.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryItemDao {
    @Query("SELECT * from groceryItem")
    fun getAll(): Flow<List<GroceryItem>>

    @Upsert
    suspend fun insertOrUpdate(vararg groceryItems: GroceryItem)

    @Delete
    suspend fun delete(groceryItem: GroceryItem)
}
