package ru.andreewkov.animations.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> GridContentWidget(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    GridWidget(
        columns = 2,
        itemCount = items.size,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) { index ->
        Box(modifier = Modifier) {
            content(items[index])
        }
    }
}

@Composable
fun GridWidget(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    Column {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns
            Row(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ){
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}