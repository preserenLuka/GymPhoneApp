package com.example.app.library.collection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CollectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Collection",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Here you’ll later list workout templates and day combinations.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
