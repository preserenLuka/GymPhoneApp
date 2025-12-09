package com.example.app.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.BluePrimary
import com.example.app.ui.theme.TealAccent
import com.example.app.ui.theme.YellowAccent

@Composable
fun StatsScreen() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Stats",
            style = MaterialTheme.typography.titleLarge,
            color = colors.onBackground
        )

        WeeklyOverviewCard()
        WorkoutFocusCard()
        ConsistencyStatsCard()
    }
}

/* ---------- UNIVERSAL CARD ---------- */
@Composable
private fun StatsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/* ---------- CARD 1: WEEKLY OVERVIEW ---------- */
@Composable
private fun WeeklyOverviewCard() {
    StatsCard(Modifier.fillMaxWidth()) {

        Text(
            "This week",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatChip("Workouts", "4", "sessions")
            StatChip("Time", "5h 10m", "total")
            StatChip("Calories", "7850", "burned")
        }

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        Text(
            "Last 7 days activity",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            val days = listOf("M", "T", "W", "T", "F", "S", "S")
            val values = listOf(0.5f, 0.8f, 0.3f, 0.9f, 0.7f, 0.0f, 0.6f)

            days.indices.forEach { i ->
                DayBar(
                    label = days[i],
                    fraction = values[i],
                    modifier = Modifier.weight(1f)
                )
                if (i != days.lastIndex) Spacer(Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, description: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DayBar(label: String, fraction: Float, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(999.dp))
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction)
                    .clip(RoundedCornerShape(999.dp))
                    .background(BluePrimary)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

/* ---------- CARD 2: WORKOUT FOCUS ---------- */
@Composable
private fun WorkoutFocusCard() {
    StatsCard(Modifier.fillMaxWidth()) {

        Text(
            "Workout focus",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(Modifier.height(12.dp))

        FocusRow("Strength", 60, BluePrimary)
        Spacer(Modifier.height(8.dp))
        FocusRow("Cardio", 25, TealAccent)
        Spacer(Modifier.height(8.dp))
        FocusRow("Mobility", 15, YellowAccent)
    }
}

@Composable
private fun FocusRow(label: String, percent: Int, barColor: Color) {
    val colors = MaterialTheme.colorScheme

    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(label)
            Text("$percent%")
        }

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(colors.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percent / 100f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(barColor)
            )
        }
    }
}

/* ---------- CARD 3: CONSISTENCY ---------- */
@Composable
private fun ConsistencyStatsCard() {
    StatsCard(Modifier.fillMaxWidth()) {

        Text(
            "Consistency",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(Modifier.height(12.dp))

        ConsistencyRow("Nutrition", 5, "on track")
        Divider(Modifier.padding(vertical = 8.dp))
        ConsistencyRow("Weights", 3, "good")
        Divider(Modifier.padding(vertical = 8.dp))
        ConsistencyRow("Jump training", 1, "getting started")
    }
}

@Composable
private fun ConsistencyRow(title: String, weeks: Int, tag: String) {
    val colors = MaterialTheme.colorScheme

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Column(Modifier.weight(1f)) {
            Text(title)
            Text(
                "Consistent for $weeks weeks",
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurface.copy(alpha = 0.75f)
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(colors.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(tag, textAlign = TextAlign.Center)
        }
    }
}
