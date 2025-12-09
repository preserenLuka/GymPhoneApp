package com.example.app.library.exercises

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URL

object ExerciseApi {

    private const val URL_JSON =
        "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/dist/exercises.json"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun fetchExercises(): List<Exercise> {
        val jsonText = URL(URL_JSON).readText()

        // API vrne [ {...}, {...}, ... ]
        return json.decodeFromString(
            ListSerializer(Exercise.serializer()),
            jsonText
        )
    }
}
