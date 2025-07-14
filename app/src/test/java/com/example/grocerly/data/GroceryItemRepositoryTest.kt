package com.example.grocerly.data

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GroceryItemRepositoryTest {

    private lateinit var dao: GroceryItemDao
    private lateinit var repository: GroceryItemRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = GroceryItemRepository(dao)
    }

    @Test
    fun deletesLastItemWithoutUpdatingOthers() = runTest {
        val item = createGroceryItem(id = 2, name = "Eggs", positionIndex = 2)
        val allItems = listOf(
            createGroceryItem(id = 0, name = "Bread", positionIndex = 0),
            createGroceryItem(id = 1, name = "Milk", positionIndex = 1),
            item
        )
        coEvery { dao.getAll() } returns allItems

        repository.deleteGroceryItem(item)

        coVerify { dao.delete(item) }
        coVerify(exactly = 0) { dao.insertOrUpdate(any()) }
    }

    @Test
    fun deletesMiddleItemAndUpdatesFollowingPositions() = runTest {
        val item = createGroceryItem(id = 1, name = "Milk", positionIndex = 1)
        val allItems = listOf(
            createGroceryItem(id = 0, name = "Bread", positionIndex = 0),
            item,
            createGroceryItem(id = 2, name = "Eggs", positionIndex = 2)
        )
        val updatedEggs = createGroceryItem(id = 2, name = "Eggs", positionIndex = 1)
        coEvery { dao.getAll() } returns allItems

        repository.deleteGroceryItem(item)

        coVerify { dao.insertOrUpdate(*arrayOf(updatedEggs)) }
        coVerify { dao.delete(item) }
    }

    @Test
    fun deletesFirstItemAndUpdatesFollowingPositions() = runTest {
        val item = createGroceryItem(id = 0, name = "Bread", positionIndex = 0)
        val allItems = listOf(
            item,
            createGroceryItem(id = 1, name = "Milk", positionIndex = 1),
            createGroceryItem(id = 2, name = "Eggs", positionIndex = 2)
        )
        val updatedMilk = createGroceryItem(id = 1, name = "Milk", positionIndex = 0)
        val updatedEggs = createGroceryItem(id = 2, name = "Eggs", positionIndex = 1)
        coEvery { dao.getAll() } returns allItems

        repository.deleteGroceryItem(item)

        coVerify { dao.insertOrUpdate(*arrayOf(updatedMilk, updatedEggs)) }
        coVerify { dao.delete(item) }
    }
}
