package com.example.app.library.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseCard(
    ex: Exercise,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 3.dp
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                ex.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Primary: ${ex.primaryMuscles.joinToString(", ")}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurface.copy(alpha = 0.75f)
            )

            Text(
                "Level: ${ex.level} • Equipment: ${ex.equipment}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}
