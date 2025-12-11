package com.example.app.library.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NutritionLibraryScreen(
    items: MutableList<String>,
    onBackClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Nutrition sets",
                style = MaterialTheme.typography.titleLarge,
                color = colors.onBackground
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showAddDialog = true },
            shape = RoundedCornerShape(999.dp)
        ) {
            Text("Add new day set")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Text(
                text = "No nutrition day sets yet. Use the button above to add one.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { name ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val trimmed = newName.trim()
                        if (trimmed.isNotEmpty()) {
                            items.add(trimmed)
                        }
                        newName = ""
                        showAddDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    newName = ""
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("New nutrition day set") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Set name") }
                )
            }
        )
    }
}
