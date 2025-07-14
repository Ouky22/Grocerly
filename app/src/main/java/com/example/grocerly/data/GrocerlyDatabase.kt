package com.example.grocerly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GroceryItem::class], version = 1)
abstract class GrocerlyDatabase : RoomDatabase() {
    abstract val groceryItemDao: GroceryItemDao

    companion object {
        @Volatile
        private var INSTANCE: GrocerlyDatabase? = null

        const val DATABASE_NAME = "grocerly_database"

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                GrocerlyDatabase::class.java,
                DATABASE_NAME,
            ).build()

            INSTANCE = instance
            instance
        }
    }
}
