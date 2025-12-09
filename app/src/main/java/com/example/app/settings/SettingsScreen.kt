package com.example.app.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.OutlineGray
import com.example.app.ui.theme.ThemeOption

@Composable
fun SettingsScreen(
    currentTheme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
    onLogoutClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    // Local UI state samo za switch-e
    var notificationsEnabled by rememberSaveable { androidx.compose.runtime.mutableStateOf(true) }
    var workoutRemindersEnabled by rememberSaveable { androidx.compose.runtime.mutableStateOf(true) }
    var wifiOnlySync by rememberSaveable { androidx.compose.runtime.mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(1.dp, OutlineGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // User row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = colorScheme.primary.copy(alpha = 0.12f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User avatar",
                                tint = colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Marjan blazic",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = "Personalize",
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Button(
                            onClick = { onLogoutClick() }, // samo redirect na login
                            shape = RoundedCornerShape(999.dp),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(
                                text = "Log out",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Theme switching
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleSmall,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ThemeSelectorRow(
                        selectedTheme = currentTheme,
                        onThemeSelected = onThemeChange
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Extra settings
                    Text(
                        text = "App settings",
                        style = MaterialTheme.typography.titleSmall,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingsToggleRow(
                        label = "Notifications",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    SettingsToggleRow(
                        label = "Workout reminders",
                        checked = workoutRemindersEnabled,
                        onCheckedChange = { workoutRemindersEnabled = it }
                    )
                    SettingsToggleRow(
                        label = "Wi-Fi only sync",
                        checked = wifiOnlySync,
                        onCheckedChange = { wifiOnlySync = it }
                    )
                }
            }
        }
    }
}

// reusable component – higher-order funkcija: onCheckedChange lambda
@Composable
fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ThemeSelectorRow(
    selectedTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ThemeChip(
            option = ThemeOption.LIGHT,
            label = "Light",
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected,
            modifier = Modifier.weight(1f)
        )
        ThemeChip(
            option = ThemeOption.DARK,
            label = "Dark",
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected,
            modifier = Modifier.weight(1f)
        )
        ThemeChip(
            option = ThemeOption.NIGHT_BLUE,
            label = "Nord",
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected,
            modifier = Modifier.weight(1f)
        )
        ThemeChip(
            option = ThemeOption.JADE_GREEN,
            label = "Jade green",
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ThemeChip(
    option: ThemeOption,
    label: String,
    selectedTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = option == selectedTheme
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (selected) colors.primary.copy(alpha = 0.12f)
                else colors.surfaceVariant
            )
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) colors.primary else OutlineGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(999.dp)
            )
            .clickable { onThemeSelected(option) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) colors.primary else colors.onSurface.copy(alpha = 0.8f)
        )
    }
}
