package com.example.grocerly

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocerly.ui.theme.GrocerlyTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


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
    modifier: Modifier = Modifier,
) {
    Scaffold { contentPadding ->
        Column(
            modifier = modifier.padding(contentPadding)
        ) {
            val hapticFeedback = LocalHapticFeedback.current
            var list by remember { mutableStateOf(List(100) { "Item $it" }) }
            val lazyListState = rememberLazyListState()
            val reorderableLazyListState =
                rememberReorderableLazyListState(lazyListState) { from, to ->
                    list = list.toMutableList().apply {
                        add(to.index, removeAt(from.index))
                    }

                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(list, key = { it }) {
                    ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
                        GroceryListItem(
                            modifier = Modifier.longPressDraggableHandle(
                                onDragStarted = {
                                    hapticFeedback.performHapticFeedback(
                                        HapticFeedbackType.GestureThresholdActivate
                                    )
                                },
                                onDragStopped = {
                                    hapticFeedback.performHapticFeedback(
                                        HapticFeedbackType.GestureEnd
                                    )
                                },
                            ),
                            isDragging = isDragging,
                            groceryItem = it,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryListItem(
    modifier: Modifier = Modifier,
    isDragging: Boolean,
    groceryItem: String,
) {
    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

    Surface(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shadowElevation = elevation,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                groceryItem,
                Modifier
                    .padding(horizontal = 8.dp),
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {},
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_drag_handle_24),
                    contentDescription = "Reorder"
                )
            }
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
