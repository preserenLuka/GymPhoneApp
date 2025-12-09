package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.BluePrimary
import com.example.app.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Force LIGHT theme so background is your E8EAF0
            AppTheme(darkTheme = false, dynamicColor = false) {
                AppRoot()
            }
        }
    }
}

/**
 * Root – decides between Login and main app.
 */
@Composable
fun AppRoot() {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { isLoggedIn = true }
        )
    } else {
        MainScaffold()
    }
}

/**
 * Main scaffold with rounded dark bar and center circular "+" FAB.
 */
@Composable
fun MainScaffold() {
    var currentDestination by rememberSaveable { mutableStateOf(BottomDestination.HOME) }

    val barColor = Color(0xFF2A2A2A)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // KEEP your original vertical sizing: 108–128dp tall outer box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
            ) {
                // Custom bar to control width/spacing
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(108.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp
                            )
                        )
                        .background(barColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp), // side padding like Figma
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 5 slots: each one 74dp wide
                        BottomBarItem(
                            destination = BottomDestination.HOME,
                            selected = currentDestination == BottomDestination.HOME,
                            onClick = { currentDestination = BottomDestination.HOME }
                        )

                        BottomBarItem(
                            destination = BottomDestination.LIBRARY,
                            selected = currentDestination == BottomDestination.LIBRARY,
                            onClick = { currentDestination = BottomDestination.LIBRARY }
                        )

                        // Centre slot under FAB (empty, just spacing)
                        Spacer(
                            modifier = Modifier
                                .width(74.dp)
                                .height(56.dp)
                        )

                        BottomBarItem(
                            destination = BottomDestination.STATS,
                            selected = currentDestination == BottomDestination.STATS,
                            onClick = { currentDestination = BottomDestination.STATS }
                        )

                        BottomBarItem(
                            destination = BottomDestination.SETTINGS,
                            selected = currentDestination == BottomDestination.SETTINGS,
                            onClick = { currentDestination = BottomDestination.SETTINGS }
                        )
                    }
                }

                // FAB – 63dp circle with 2dp dark border
                FloatingActionButton(
                    onClick = { /* TODO: open Add flow */ },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(63.dp),
                    shape = CircleShape,
                    containerColor = BluePrimary,
                    contentColor = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, barColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (currentDestination) {
                BottomDestination.HOME -> HomeScreen()
                BottomDestination.LIBRARY -> LibraryScreen()
                BottomDestination.STATS -> StatsScreen()
                BottomDestination.SETTINGS -> SettingsScreen()   // IMPORT from ui.settings
            }
        }
    }
}

/**
 * One bottom bar item. Icon + text in a 74dp-wide box,
 * with the content itself roughly 52×36dp centered inside.
 */
@Composable
fun BottomBarItem(
    destination: BottomDestination,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(74.dp)               // slot width
            .height(66.dp)              // close to your 52×36 content box
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = destination.label,
                tint = if (selected) BluePrimary else Color.White
            )
            Text(
                text = destination.label,
                color = if (selected) BluePrimary else Color.White
            )
        }
    }
}

/**
 * Bottom navigation destinations.
 */
enum class BottomDestination(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Filled.Home),
    LIBRARY("Library", Icons.Filled.List),
    STATS("Stats", Icons.Filled.ShowChart),
    SETTINGS("Settings", Icons.Filled.Settings),
}

/* Temporary placeholders for screens */

@Composable
fun HomeScreen() {
    Text("Home screen")
}

@Composable
fun LibraryScreen() {
    Text("Library screen")
}

@Composable
fun StatsScreen() {
    Text("Stats screen")
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme(darkTheme = false, dynamicColor = false) {
        AppRoot()
    }
}
