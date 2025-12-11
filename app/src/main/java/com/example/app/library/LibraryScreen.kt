package com.example.app.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.app.library.collection.CollectionScreen
import com.example.app.library.exercises.ExercisesScreen
import com.example.app.library.nutrition.NutritionLibraryScreen
import com.example.app.library.schedule.ScheduleScreen

enum class LibrarySection {
    ROOT,
    EXERCISES,
    RECIPES,
    NUTRITION,
    SCHEDULE
}

enum class LibraryQuickAddTarget {
    EXERCISES,
    RECIPES,
    NUTRITION,
    SCHEDULE
}

@Composable
fun LibraryScreen(
    recipeTemplates: MutableList<String>,
    nutritionSets: MutableList<String>,
    scheduleSets: MutableList<String>,
    quickAddTarget: LibraryQuickAddTarget?,
    onQuickAddHandled: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    var section by rememberSaveable { mutableStateOf(LibrarySection.ROOT) }

    // React to quickAddTarget coming from the global + popup
    LaunchedEffect(quickAddTarget) {
        if (quickAddTarget != null) {
            section = when (quickAddTarget) {
                LibraryQuickAddTarget.EXERCISES -> LibrarySection.EXERCISES
                LibraryQuickAddTarget.RECIPES -> LibrarySection.RECIPES
                LibraryQuickAddTarget.NUTRITION -> LibrarySection.NUTRITION
                LibraryQuickAddTarget.SCHEDULE -> LibrarySection.SCHEDULE
            }
            onQuickAddHandled()
        }
    }

    when (section) {
        LibrarySection.ROOT -> {
            LibraryRootScreen(
                recipeCount = recipeTemplates.size,
                nutritionCount = nutritionSets.size,
                scheduleCount = scheduleSets.size,
                onOpenExercises = { section = LibrarySection.EXERCISES },
                onOpenRecipes = { section = LibrarySection.RECIPES },
                onOpenNutrition = { section = LibrarySection.NUTRITION },
                onOpenSchedule = { section = LibrarySection.SCHEDULE }
            )
        }

        LibrarySection.EXERCISES -> {
            ExercisesScreen(
                onBackClick = { section = LibrarySection.ROOT }
            )
        }

        LibrarySection.RECIPES -> {
            CollectionScreen(
                items = recipeTemplates,
                onBackClick = { section = LibrarySection.ROOT }
            )
        }

        LibrarySection.NUTRITION -> {
            NutritionLibraryScreen(
                items = nutritionSets,
                onBackClick = { section = LibrarySection.ROOT }
            )
        }

        LibrarySection.SCHEDULE -> {
            ScheduleScreen(
                items = scheduleSets,
                onBackClick = { section = LibrarySection.ROOT }
            )
        }
    }
}

@Composable
private fun LibraryRootScreen(
    recipeCount: Int,
    nutritionCount: Int,
    scheduleCount: Int,
    onOpenExercises: () -> Unit,
    onOpenRecipes: () -> Unit,
    onOpenNutrition: () -> Unit,
    onOpenSchedule: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Library",
            style = MaterialTheme.typography.titleLarge,
            color = colors.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LibraryCard(
                title = "Exercises",
                subtitle = "All individual workouts, searchable and filterable",
                badge = "API data",
                gradient = Brush.horizontalGradient(
                    listOf(Color(0xFF8FC5FF), Color(0xFF4F8CFF))
                ),
                icon = Icons.Filled.FitnessCenter,
                onClick = onOpenExercises
            )
            LibraryCard(
                title = "Recipes",
                subtitle = "Workout templates & day combinations",
                badge = "$recipeCount templates",
                gradient = Brush.horizontalGradient(
                    listOf(Color(0xFFB79CFF), Color(0xFF8D63FF))
                ),
                icon = Icons.Filled.MenuBook,
                onClick = onOpenRecipes
            )
            LibraryCard(
                title = "Nutrition",
                subtitle = "Foods, meals, liquid tracking & limits",
                badge = "$nutritionCount daily sets",
                gradient = Brush.horizontalGradient(
                    listOf(Color(0xFF8FE8C0), Color(0xFF46C98E))
                ),
                icon = Icons.Filled.Restaurant,
                onClick = onOpenNutrition
            )
            LibraryCard(
                title = "Schedule",
                subtitle = "Weekly workout plan & daily assignments",
                badge = "$scheduleCount sets",
                gradient = Brush.horizontalGradient(
                    listOf(Color(0xFFF5B06A), Color(0xFFE27D3F))
                ),
                icon = Icons.Filled.CalendarMonth,
                onClick = onOpenSchedule
            )
        }
    }
}

@Composable
private fun LibraryCard(
    title: String,
    subtitle: String,
    badge: String,
    gradient: Brush,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(brush = gradient)
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.onSurface
                    )
                }
            }
        }
    }
}
