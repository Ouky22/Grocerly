package com.example.grocerly

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.grocerly.data.GroceryItem
import com.example.grocerly.ui.theme.GrocerlyTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


@Composable
fun GroceryListScreen(
    modifier: Modifier,
    viewModel: GroceryListViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GroceryListScreen(
        modifier = modifier,
        state = state,
        onReorderGroceryItem = { from, to ->
            viewModel.onEvent(GroceryListEvent.ReorderGroceryItem(from, to))
        },
        onAddNewGroceryItemClick = { viewModel.onEvent(GroceryListEvent.ShowAddDialog) },
        onUpdateNewGroceryItemName = { newName ->
            viewModel.onEvent(GroceryListEvent.UpdateNewGroceryItemName(newName))
        },
        onUpdateNewGroceryItemQuantity = { newQuantity ->
            viewModel.onEvent(GroceryListEvent.UpdateNewGroceryItemQuantity(newQuantity))
        },
        onSubmitNewGroceryItem = { viewModel.onEvent(GroceryListEvent.AddGroceryItem) },
        onDismissAddGroceryItemDialog = {
            viewModel.onEvent(GroceryListEvent.HideAddDialog)
        },
        onGroceryItemDeleteClick = { groceryItem ->
            viewModel.onEvent(GroceryListEvent.DeleteGroceryItem(groceryItem))
        },
        onCheckOffGroceryItem = { groceryItem ->
            viewModel.onEvent(GroceryListEvent.CheckOffGroceryItem(groceryItem))
        },
        onToggleEditingMode = { viewModel.onEvent(GroceryListEvent.ToggleEditMode) }
    )
}

@Composable
fun GroceryListScreen(
    modifier: Modifier = Modifier,
    state: GroceryListState,
    onReorderGroceryItem: (from: Int, to: Int) -> Unit,
    onAddNewGroceryItemClick: () -> Unit,
    onUpdateNewGroceryItemName: (newName: String) -> Unit,
    onUpdateNewGroceryItemQuantity: (newQuantity: Int) -> Unit,
    onSubmitNewGroceryItem: () -> Unit,
    onDismissAddGroceryItemDialog: () -> Unit,
    onGroceryItemDeleteClick: (groceryItem: GroceryItem) -> Unit,
    onCheckOffGroceryItem: (groceryItem: GroceryItem) -> Unit,
    onToggleEditingMode: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState =
        rememberReorderableLazyListState(lazyListState) { from, to ->
            if (state.inEditMode) {
                onReorderGroceryItem(from.index, to.index)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
        ) {
            items(state.groceryItems, key = { it.id }) {
                ReorderableItem(reorderableLazyListState, key = it.id) { isDragging ->
                    GroceryListItem(
                        modifier = if (state.inEditMode) {
                            Modifier.longPressDraggableHandle(
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
                            )
                        } else {
                            Modifier // No drag handle when not in edit mode
                        },
                        isDragging = isDragging,
                        groceryItem = it,
                        onClick = onCheckOffGroceryItem,
                        onDeleteClick = onGroceryItemDeleteClick,
                        inEditMode = state.inEditMode,
                    )
                }
            }
        }

        BottomButtonRow(
            onAddNewGroceryItemClick = onAddNewGroceryItemClick,
            onToggleEditingMode = onToggleEditingMode,
            inEditingMode = state.inEditMode,
        )
    }

    if (state.showAddGroceryItemDialog) {
        AddGroceryItemDialog(
            name = state.newGroceryItemName,
            quantity = state.newGroceryItemQuantity,
            onNameChange = onUpdateNewGroceryItemName,
            onQuantityChange = onUpdateNewGroceryItemQuantity,
            onSubmit = onSubmitNewGroceryItem,
            onDismiss = onDismissAddGroceryItemDialog,
        )
    }
}

@Composable
fun BottomButtonRow(
    modifier: Modifier = Modifier,
    onAddNewGroceryItemClick: () -> Unit,
    onToggleEditingMode: () -> Unit,
    inEditingMode: Boolean,
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!inEditingMode) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onToggleEditingMode,
        ) {
            Icon(
                imageVector = if (inEditingMode) Icons.Default.Lock else Icons.Default.Edit,
                contentDescription = "Enable or disable editing mode"
            )
        }

        if (!inEditingMode) {
            Spacer(modifier = Modifier.weight(1f))
        }

        if (inEditingMode) {
            Button(
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 16.dp),
                onClick = onAddNewGroceryItemClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_grocery_item_to_list)
                )
            }
        }
    }
}

@Composable
fun GroceryListItem(
    modifier: Modifier = Modifier,
    isDragging: Boolean,
    groceryItem: GroceryItem,
    onClick: (GroceryItem) -> Unit,
    onDeleteClick: (GroceryItem) -> Unit,
    inEditMode: Boolean,
) {
    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!inEditMode) {
                    onClick(groceryItem)
                }
            },
        shadowElevation = elevation,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${groceryItem.quantity}x",
                    modifier = Modifier.width(42.dp), // Adjust width as needed
                    fontSize = 20.sp,
                    color = if (groceryItem.checkedOff)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = groceryItem.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration =
                        if (groceryItem.checkedOff) TextDecoration.LineThrough
                        else TextDecoration.None,
                    color = if (groceryItem.checkedOff)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            if (inEditMode) {
                IconButton(
                    onClick = { onDeleteClick(groceryItem) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_item)
                    )
                }
            }
        }
    }
}

@Composable
fun AddGroceryItemDialog(
    modifier: Modifier = Modifier,
    name: String,
    quantity: Int,
    onNameChange: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(modifier = modifier) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.grocery_name)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(bottom = 16.dp)
                )

                Text(
                    stringResource(R.string.quantity, quantity),
                    modifier = Modifier.padding(top = 16.dp)
                )

                Slider(
                    value = quantity.toFloat(),
                    onValueChange = { onQuantityChange(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
                    Button(
                        onClick = onSubmit,
                        enabled = name.isNotBlank(),
                        modifier = Modifier.padding(start = 8.dp)
                    ) { Text(stringResource(R.string.add)) }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroceryListScreenPreview() {
    GrocerlyTheme {
        GroceryListScreen(
            modifier = Modifier.fillMaxSize(),
            onReorderGroceryItem = { _, _ -> },
            onAddNewGroceryItemClick = { },
            onUpdateNewGroceryItemName = { },
            onUpdateNewGroceryItemQuantity = { },
            onSubmitNewGroceryItem = { },
            onDismissAddGroceryItemDialog = { },
            onGroceryItemDeleteClick = {},
            onCheckOffGroceryItem = {},
            onToggleEditingMode = {},
            state = GroceryListState(
                groceryItems = listOf(
                    GroceryItem(id = 1L, name = "Apples", quantity = 5, positionIndex = 0),
                    GroceryItem(id = 2L, name = "Bananas", quantity = 7, positionIndex = 1),
                    GroceryItem(id = 3L, name = "Carrots", quantity = 3, positionIndex = 2),
                    GroceryItem(id = 4L, name = "Bread", quantity = 2, positionIndex = 3),
                )
            ),
        )
    }
}
