package com.example.app.library.exercises

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val force: String? = null,
    val level: String? = null,
    val mechanic: String? = null,
    val equipment: String? = null,
    val primaryMuscles: List<String> = emptyList(),
    val secondaryMuscles: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val category: String? = null,
    val images: List<String> = emptyList(),
    val id: String? = null
)
