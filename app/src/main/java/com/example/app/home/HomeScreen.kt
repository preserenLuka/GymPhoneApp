package com.example.app.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.BluePrimary
import com.example.app.ui.theme.YellowAccent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(onSeeStatsClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    // STATE
    var isWorkoutStarted by rememberSaveable { mutableStateOf(false) }      // workout started?
    var popupType by rememberSaveable { mutableStateOf<PopupType?>(null) }  // current popup, null = none

    val isPopupVisible = popupType != null

    // Today label (e.g. "11. december")
    val todayLabel = remember {
        LocalDate.now().format(
            DateTimeFormatter.ofPattern("d. MMMM", Locale("sl"))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // MAIN SCROLL CONTENT (blurred when popup is visible)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isPopupVisible) Modifier.blur(18.dp) else Modifier
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TodayDateCard(dateLabel = todayLabel)

            TodayWorkoutCard(
                isStarted = isWorkoutStarted,
                onStart = { isWorkoutStarted = true },
                onCancel = { isWorkoutStarted = false }
            )
            NutritionCard(
                onLogMealClick = { popupType = PopupType.LogMeal },
                onLogLiquidClick = { popupType = PopupType.LogLiquid }
            )
            ProgressCard(
                onSeeStatsClick = { section ->
                    popupType = PopupType.Stats(section)
                }
            )
        }

        // POPUP OVERLAY
        if (isPopupVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)), // dim, nav bar ostane brez
                contentAlignment = Alignment.Center
            ) {
                PopupCard(
                    popupType = popupType!!,
                    onClose = { popupType = null }
                )
            }
        }
    }
}

/* ---------- Helper types ---------- */

sealed class PopupType {
    object LogMeal : PopupType()
    object LogLiquid : PopupType()
    data class Stats(val sectionName: String) : PopupType()
}

/* ---------- Reusable "card" wrapper ---------- */

@Composable
private fun SmartCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colors.surface,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/* ---------- Small date card (full-width, subtle, centered) ---------- */

@Composable
private fun TodayDateCard(
    dateLabel: String
) {
    SmartCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dateLabel,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}


/* ---------- POPUP CARD ---------- */

@Composable
private fun PopupCard(
    popupType: PopupType,
    onClose: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val title: String
            val description: String

            when (popupType) {
                PopupType.LogMeal -> {
                    title = "Log meal"
                    description = "Template for adding a meal today. You’ll connect this later to real data."
                }
                PopupType.LogLiquid -> {
                    title = "Log liquid"
                    description = "Template for adding liquid intake. Use it later to track water or drinks."
                }
                is PopupType.Stats -> {
                    title = "${popupType.sectionName} stats"
                    description = "Generic stats popup. Here you can later show charts or history for this section."
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurface.copy(alpha = 0.8f)
            )

            // simple template fields (nothing actually wired yet)
            OutlinedTextField(
                value = "",
                onValueChange = { },
                label = { Text("Title / name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            OutlinedTextField(
                value = "",
                onValueChange = { },
                label = { Text("Amount / notes") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onClose) {
                    Text("Close")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onClose,
                    enabled = false
                ) {
                    Text("Save (later)")
                }
            }
        }
    }
}

/* ---------- Card 2: Today's workout ---------- */

@Composable
private fun TodayWorkoutCard(
    isStarted: Boolean,
    onStart: () -> Unit,
    onCancel: () -> Unit
) {
    SmartCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Leg day + injury prevention",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "1h 25min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.weight(1f))

            if (!isStarted) {
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Start workout",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Workout started",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(999.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

/* ---------- Card 3: Nutrition (Macros + Nutrition) ---------- */

@Composable
private fun NutritionCard(
    onLogMealClick: () -> Unit,
    onLogLiquidClick: () -> Unit
) {
    SmartCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Macros plan",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Total daily: 2210 Cal.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        MacroRow(name = "Protein", grams = "135 g", percent = 58, barColor = BluePrimary)
        Spacer(modifier = Modifier.height(6.dp))
        MacroRow(name = "Fat", grams = "92 g", percent = 43, barColor = YellowAccent)
        Spacer(modifier = Modifier.height(6.dp))
        MacroRow(name = "Carbs", grams = "56 g", percent = 76, barColor = MaterialTheme.colorScheme.secondary)

        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nutrition",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2650 Cal.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onLogMealClick,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowAccent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log meal", textAlign = TextAlign.Center)
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2150 ml.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onLogLiquidClick,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log liquid", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun MacroRow(
    name: String,
    grams: String,
    percent: Int,
    barColor: Color
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            Text(
                text = grams,
                style = MaterialTheme.typography.labelMedium,
                color = textColor.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percent / 100f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(barColor)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$percent%",
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}

/* ---------- Card 4: Progress ---------- */

@Composable
private fun ProgressCard(
    onSeeStatsClick: (sectionName: String) -> Unit
) {
    SmartCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProgressRow(
            title = "Nutrition",
            weeks = 2,
            buttonLabel = "See stats",
            buttonColor = BluePrimary,
            onSeeStatsClick = onSeeStatsClick
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ProgressRow(
            title = "Weights",
            weeks = 3,
            buttonLabel = "See stats",
            buttonColor = YellowAccent,
            onSeeStatsClick = onSeeStatsClick
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ProgressRow(
            title = "Jump training",
            weeks = 7,
            buttonLabel = "See stats",
            buttonColor = BluePrimary,
            onSeeStatsClick = onSeeStatsClick
        )
    }
}

@Composable
private fun ProgressRow(
    title: String,
    weeks: Int,
    buttonLabel: String,
    buttonColor: Color,
    onSeeStatsClick: (sectionName: String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // NOT a button, samo vizualni badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Consistent for $weeks weeks",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        Button(
            onClick = { onSeeStatsClick(title) },
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = colors.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(buttonLabel, style = MaterialTheme.typography.labelLarge)
        }
    }
}
