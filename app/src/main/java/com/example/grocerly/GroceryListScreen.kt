package com.example.grocerly

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.grocerly.ui.theme.GrocerlyTheme


@Composable
fun GroceryListScreen(
    modifier: Modifier,
    viewModel: GroceryListViewModel,
) {
    GroceryListScreen(
        modifier = modifier,
    )
}

@Composable
fun GroceryListScreen(
    modifier: Modifier,
) {
    Scaffold { contentPadding ->
        Column(
            modifier = modifier.padding(contentPadding)
        ) {
            Text("Hello")
        }
    }
}

@Preview
@Composable
fun GroceryListScreenPreview() {
    GrocerlyTheme {
        GroceryListScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
