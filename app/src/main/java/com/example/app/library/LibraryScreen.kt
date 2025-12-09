package com.example.app.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.library.collection.CollectionScreen
import com.example.app.library.exercises.ExercisesScreen
import com.example.app.library.nutrition.NutritionLibraryScreen
import com.example.app.library.schedule.ScheduleScreen
import com.example.app.ui.theme.*



enum class LibrarySection {
    EXERCISES,
    COLLECTION,
    NUTRITION,
    SCHEDULE
}

@Composable
fun LibraryScreen() {
    val colors = MaterialTheme.colorScheme
    var currentSection by rememberSaveable { mutableStateOf<LibrarySection?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        when (currentSection) {
            null -> LibraryOverview(
                onSectionClick = { currentSection = it }
            )

            LibrarySection.EXERCISES -> SectionContainer(
                title = "Exercises",
                onBack = { currentSection = null }
            ) { ExercisesScreen() }

            LibrarySection.COLLECTION -> SectionContainer(
                title = "Collection",
                onBack = { currentSection = null }
            ) { CollectionScreen() }

            LibrarySection.NUTRITION -> SectionContainer(
                title = "Nutrition",
                onBack = { currentSection = null }
            ) { NutritionLibraryScreen() }

            LibrarySection.SCHEDULE -> SectionContainer(
                title = "Schedule",
                onBack = { currentSection = null }
            ) { ScheduleScreen() }
        }
    }
}

/* ---------- Overview with 4 cards filling height ---------- */

@Composable
private fun LibraryOverview(
    onSectionClick: (LibrarySection) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Library",
            style = MaterialTheme.typography.titleLarge,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LibraryCard(
                title = "Exercises",
                subtitle = "All individual workouts,\nsearchable and filterable",
                countLabel = "124 entries",
                icon = Icons.Filled.FitnessCenter,
                gradient = Brush.horizontalGradient(
                    listOf(BluePrimary, BlueSecondary)
                ),
                modifier = Modifier.weight(1f),
                onClick = { onSectionClick(LibrarySection.EXERCISES) }
            )

            LibraryCard(
                title = "Collection",
                subtitle = "Workout templates &\nday combinations",
                countLabel = "18 templates",
                icon = Icons.Filled.MenuBook,
                gradient = Brush.horizontalGradient(
                    listOf(PurpleAccent, BlueSecondary)
                ),
                modifier = Modifier.weight(1f),
                onClick = { onSectionClick(LibrarySection.COLLECTION) }
            )

            LibraryCard(
                title = "Nutrition",
                subtitle = "Foods, meals, liquid\ntracking & limits",
                countLabel = "4 daily sets",
                icon = Icons.Filled.Restaurant,
                gradient = Brush.horizontalGradient(
                    listOf(TealAccent, BlueSecondary)
                ),
                modifier = Modifier.weight(1f),
                onClick = { onSectionClick(LibrarySection.NUTRITION) }
            )

            LibraryCard(
                title = "Schedule",
                subtitle = "Weekly workout plan\n& daily assignments",
                countLabel = "4 sets",
                icon = Icons.Filled.CalendarMonth,
                gradient = Brush.horizontalGradient(
                    listOf(YellowAccent, Color(0xFFFFA040))
                ),
                modifier = Modifier.weight(1f),
                onClick = { onSectionClick(LibrarySection.SCHEDULE) }
            )
        }
    }
}

@Composable
private fun LibraryCard(
    title: String,
    subtitle: String,
    countLabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.White.copy(alpha = 0.9f)
            ) {
                Text(
                    text = countLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/* ---------- Section container with back + title ---------- */

@Composable
private fun SectionContainer(
    title: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = colors.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}
