package ru.andreewkov.composer.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitledWidget(
    text: String,
    color: Color,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        Text(
            text = text,
            color = color,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        content()
    }
}