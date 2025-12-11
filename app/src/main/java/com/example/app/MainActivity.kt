package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.home.HomeScreen
import com.example.app.library.LibraryScreen
import com.example.app.library.LibraryQuickAddTarget
import com.example.app.stats.StatsScreen
import com.example.app.settings.SettingsScreen
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.NeutralDark
import com.example.app.ui.theme.PureWhite
import com.example.app.ui.theme.ThemeOption
import com.example.app.ui.theme.BluePrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isLoggedIn by rememberSaveable { mutableStateOf(false) }
            var currentTheme by rememberSaveable { mutableStateOf(ThemeOption.LIGHT) }

            AppTheme(themeOption = currentTheme, dynamicColor = false) {
                if (!isLoggedIn) {
                    LoginScreen(onLoginSuccess = { isLoggedIn = true })
                } else {
                    MainScaffold(
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it },
                        onLogout = { isLoggedIn = false }
                    )
                }
            }
        }
    }
}

enum class BottomDestination(val label: String) {
    HOME("Home"),
    LIBRARY("Library"),
    STATS("Stats"),
    SETTINGS("Settings")
}

@Composable
fun MainScaffold(
    currentTheme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
    onLogout: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    var currentDestination by rememberSaveable { mutableStateOf(BottomDestination.HOME) }

    // Shared local "library data" – used by LibraryScreen AND popup counts
    val recipeTemplates = remember { mutableStateListOf("Push day template", "Leg day + core") }
    val nutritionSets = remember { mutableStateListOf("Clean bulk day", "Rest day deficit") }
    val scheduleSets = remember { mutableStateListOf("Week A", "Week B") }

    // Global + popup state
    var showAddMenu by rememberSaveable { mutableStateOf(false) }
    var quickAddTarget by rememberSaveable { mutableStateOf<LibraryQuickAddTarget?>(null) }

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                onDestinationSelected = { currentDestination = it },
                onFabClick = { showAddMenu = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                BottomDestination.HOME -> {
                    HomeScreen(
                        onSeeStatsClick = {
                            currentDestination = BottomDestination.STATS
                        }
                    )
                }

                BottomDestination.LIBRARY -> {
                    LibraryScreen(
                        recipeTemplates = recipeTemplates,
                        nutritionSets = nutritionSets,
                        scheduleSets = scheduleSets,
                        quickAddTarget = quickAddTarget,
                        onQuickAddHandled = { quickAddTarget = null }
                    )
                }

                BottomDestination.STATS -> {
                    StatsScreen()
                }

                BottomDestination.SETTINGS -> {
                    SettingsScreen(
                        currentTheme = currentTheme,
                        onThemeChange = onThemeChange,
                        onLogoutClick = onLogout
                    )
                }
            }

            // GLOBAL ADD POPUP (overlays everything, anchored from +)
            if (showAddMenu) {
                AddNewPopup(
                    exerciseCount = 124, // visual only; exercises come from API
                    recipeCount = recipeTemplates.size,
                    nutritionCount = nutritionSets.size,
                    scheduleCount = scheduleSets.size,
                    onAddExercise = {
                        quickAddTarget = LibraryQuickAddTarget.EXERCISES
                        currentDestination = BottomDestination.LIBRARY
                        showAddMenu = false
                    },
                    onAddRecipes = {
                        quickAddTarget = LibraryQuickAddTarget.RECIPES
                        currentDestination = BottomDestination.LIBRARY
                        showAddMenu = false
                    },
                    onAddNutrition = {
                        quickAddTarget = LibraryQuickAddTarget.NUTRITION
                        currentDestination = BottomDestination.LIBRARY
                        showAddMenu = false
                    },
                    onAddSchedule = {
                        quickAddTarget = LibraryQuickAddTarget.SCHEDULE
                        currentDestination = BottomDestination.LIBRARY
                        showAddMenu = false
                    },
                    onDismiss = { showAddMenu = false }
                )
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    currentDestination: BottomDestination,
    onDestinationSelected: (BottomDestination) -> Unit,
    onFabClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)          // whole bottom area (same as bar)
    ) {
        // Dark rounded bar at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(108.dp)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(NeutralDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    // more padding at the BOTTOM than at the TOP -> content sits higher
                    .padding(start = 12.dp, end = 12.dp, top = 2.dp, bottom = 23.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BottomBarItem(
                    icon = Icons.Filled.Home,
                    label = BottomDestination.HOME.label,
                    isSelected = currentDestination == BottomDestination.HOME,
                    onClick = { onDestinationSelected(BottomDestination.HOME) }
                )

                BottomBarItem(
                    icon = Icons.Filled.List,
                    label = BottomDestination.LIBRARY.label,
                    isSelected = currentDestination == BottomDestination.LIBRARY,
                    onClick = { onDestinationSelected(BottomDestination.LIBRARY) }
                )

                // Center slot with inline FAB
                Box(
                    modifier = Modifier
                        .width(74.dp)
                        .height(66.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(BluePrimary)
                            .clickable { onFabClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = PureWhite
                        )
                    }
                }

                BottomBarItem(
                    icon = Icons.AutoMirrored.Filled.ShowChart,
                    label = BottomDestination.STATS.label,
                    isSelected = currentDestination == BottomDestination.STATS,
                    onClick = { onDestinationSelected(BottomDestination.STATS) }
                )

                BottomBarItem(
                    icon = Icons.Filled.Settings,
                    label = BottomDestination.SETTINGS.label,
                    isSelected = currentDestination == BottomDestination.SETTINGS,
                    onClick = { onDestinationSelected(BottomDestination.SETTINGS) }
                )
            }
        }
    }
}



@Composable
private fun BottomBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val contentColor = if (isSelected) BluePrimary else Color.White

    Column(
        modifier = Modifier
            .width(74.dp)
            .height(66.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
private fun AddNewPopup(
    exerciseCount: Int,
    recipeCount: Int,
    nutritionCount: Int,
    scheduleCount: Int,
    onAddExercise: () -> Unit,
    onAddRecipes: () -> Unit,
    onAddNutrition: () -> Unit,
    onAddSchedule: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable(onClick = onDismiss, indication = null, interactionSource = remember { MutableInteractionSource() })
    ) {
        // Card panel floating above bottom bar (not even half screen)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Add new",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                AddNewCategoryCard(
                    title = "Exercise",
                    countLabel = "$exerciseCount entries",
                    icon = Icons.Filled.FitnessCenter,
                    backgroundColor = Color(0xFF8FC5FF),
                    onClick = onAddExercise
                )
                AddNewCategoryCard(
                    title = "Recipes",
                    countLabel = "$recipeCount templates",
                    icon = Icons.Filled.MenuBook,
                    backgroundColor = Color(0xFFB79CFF),
                    onClick = onAddRecipes
                )
                AddNewCategoryCard(
                    title = "Nutrition",
                    countLabel = "$nutritionCount daily sets",
                    icon = Icons.Filled.Fastfood,
                    backgroundColor = Color(0xFF8FE8C0),
                    onClick = onAddNutrition
                )
                AddNewCategoryCard(
                    title = "Schedule",
                    countLabel = "$scheduleCount sets",
                    icon = Icons.Filled.CalendarMonth,
                    backgroundColor = Color(0xFFF5B06A),
                    onClick = onAddSchedule
                )
            }
        }
    }
}

@Composable
private fun AddNewCategoryCard(
    title: String,
    countLabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black
                    )
                    Text(
                        text = countLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }

            Text(
                text = "Add new",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme(themeOption = ThemeOption.LIGHT, dynamicColor = false) {
        MainScaffold(
            currentTheme = ThemeOption.LIGHT,
            onThemeChange = {},
            onLogout = {}
        )
    }
}
