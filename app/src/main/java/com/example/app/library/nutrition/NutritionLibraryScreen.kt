package com.example.app.library.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NutritionLibraryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Nutrition",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This screen will later contain foods, meals, liquids and limits.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
