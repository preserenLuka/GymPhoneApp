package com.example.app.library.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExercisesViewModel {

    var exercises by mutableStateOf<List<Exercise>>(emptyList())
    var filtered by mutableStateOf<List<Exercise>>(emptyList())

    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)

    var filters by mutableStateOf(ExerciseFilters())

    // za popup
    var selectedExercise by mutableStateOf<Exercise?>(null)

    suspend fun load() {
        loading = true
        try {
            val data = withContext(Dispatchers.IO) {
                ExerciseApi.fetchExercises()
            }
            exercises = data
            applyFilters()
            loading = false
        } catch (e: Exception) {
            error = "Error: ${e.message}"
            e.printStackTrace()
            loading = false
        }
    }

    fun applyFilters() {
        filtered = exercises
            .asSequence()
            .filter { ex ->
                filters.search.isEmpty() ||
                        ex.name.contains(filters.search, ignoreCase = true)
            }
            .filter { ex ->
                filters.level == null || ex.level == filters.level
            }
            .filter { ex ->
                filters.equipment == null || ex.equipment == filters.equipment
            }
            .filter { ex ->
                filters.muscle == null || ex.primaryMuscles.contains(filters.muscle)
            }
            .sortedBy { it.name.lowercase() }
            .toList()
    }

    fun updateSearch(q: String) {
        filters = filters.copy(search = q)
        applyFilters()
    }

    fun setLevel(l: String?) {
        filters = filters.copy(level = l)
        applyFilters()
    }

    fun setEquipment(e: String?) {
        filters = filters.copy(equipment = e)
        applyFilters()
    }

    fun setMuscle(m: String?) {
        filters = filters.copy(muscle = m)
        applyFilters()
    }
}
