package com.example.grocerly.di

import android.content.Context
import com.example.grocerly.data.GrocerlyDatabase
import com.example.grocerly.data.GroceryItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GrocerlyDatabase {
        return GrocerlyDatabase.getInstance(context.applicationContext)
    }

    @Singleton
    @Provides
    fun provideGrocerlyItemDao(grocerlyDatabase: GrocerlyDatabase): GroceryItemDao =
        grocerlyDatabase.groceryItemDao
}
