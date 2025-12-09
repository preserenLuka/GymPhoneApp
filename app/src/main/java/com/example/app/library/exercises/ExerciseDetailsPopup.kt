package com.example.app.library.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseDetailsPopup(
    exercise: Exercise,
    onClose: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp,
            color = colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    exercise.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Primary muscles: " + exercise.primaryMuscles.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurface.copy(alpha = 0.85f)
                )

                if (exercise.secondaryMuscles.isNotEmpty()) {
                    Text(
                        "Secondary muscles: " +
                                exercise.secondaryMuscles.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Level: ${exercise.level ?: "unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Equipment: ${exercise.equipment ?: "none"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Category: ${exercise.category ?: "none"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Instructions:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(8.dp))

                for ((index, step) in exercise.instructions.withIndex()) {
                    Text(
                        "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurface.copy(alpha = 0.85f)
                    )
                    Spacer(Modifier.height(6.dp))
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}
