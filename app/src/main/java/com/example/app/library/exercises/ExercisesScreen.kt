package com.example.app.library.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ExercisesScreen(onBackClick: () -> Unit) {
    val vm = remember { ExercisesViewModel() }
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme

    var showFilters by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { scope.launch { vm.load() } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Exercises",
                style = MaterialTheme.typography.titleLarge,
                color = colors.onBackground
            )

            // SEARCH
            OutlinedTextField(
                value = vm.filters.search,
                onValueChange = { vm.updateSearch(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search exercises…") },
                trailingIcon = {
                    IconButton(onClick = { vm.updateSearch(vm.filters.search) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            // FILTER BUTTON
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { showFilters = !showFilters }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filters")
                    Spacer(Modifier.width(6.dp))
                    Text("Filters")
                }
            }

            // FILTER PANEL
            if (showFilters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterDropdown(
                        title = "Level",
                        options = levelOptions,
                        selected = vm.filters.level,
                        onSelect = { vm.setLevel(it) }
                    )
                    FilterDropdown(
                        title = "Equipment",
                        options = equipmentOptions,
                        selected = vm.filters.equipment,
                        onSelect = { vm.setEquipment(it) }
                    )
                    FilterDropdown(
                        title = "Primary muscle",
                        options = muscleOptions,
                        selected = vm.filters.muscle,
                        onSelect = { vm.setMuscle(it) }
                    )
                }
            }

            // CONTENT
            when {
                vm.loading -> {
                    Box(
                        Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                vm.error != null -> {
                    Box(
                        Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Failed to load.")
                            TextButton(onClick = { scope.launch { vm.load() } }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(vm.filtered) { ex ->
                            ExerciseCard(
                                ex = ex,
                                onClick = { vm.selectedExercise = ex }
                            )
                        }
                    }
                }
            }
        }

        // POPUP
        vm.selectedExercise?.let { ex ->
            ExerciseDetailsPopup(
                exercise = ex,
                onClose = { vm.selectedExercise = null }
            )
        }
    }
}

/* ---------- FILTER DROPDOWN ---------- */

@Composable
fun FilterDropdown(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected ?: title)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Any") },
                onClick = {
                    onSelect(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
