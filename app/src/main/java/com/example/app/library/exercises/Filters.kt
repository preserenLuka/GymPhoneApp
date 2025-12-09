package com.example.app.library.exercises

data class ExerciseFilters(
    val search: String = "",
    val level: String? = null,
    val equipment: String? = null,
    val muscle: String? = null
)

val levelOptions = listOf("beginner", "intermediate", "expert")

val equipmentOptions = listOf(
    "barbell", "dumbbell", "body only", "machine", "cable",
    "bands", "kettlebells", "other", "medicine ball",
    "exercise ball", "foam roll"
)

val muscleOptions = listOf(
    "quadriceps", "shoulders", "abdominals", "chest", "hamstrings",
    "triceps", "biceps", "lats", "middle back", "calves",
    "lower back", "forearms", "glutes", "traps",
    "adductors", "abductors", "neck"
)
