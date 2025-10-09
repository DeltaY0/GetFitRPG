package com.example.getfitrpg.backend

import com.example.getfitrpg.R
import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.io.InputStream

@Serializable
data class Exercise(
    val id: String,
    val link: String
)

class JSONSerializer(context: Context) {
    private val exerciseSet: HashSet<Exercise>

    init {
        exerciseSet = loadExercisesFromRawResource(context)
        if (exerciseSet.isEmpty()) {
            Log.e("LocalDB", "Exercise data failed to load. The set is empty.")
        } else {
            Log.d("LocalDB", "Successfully loaded and parsed ${exerciseSet.size} exercises.")
        }
    }

    fun getExerciseSet(): HashSet<Exercise> {
        return exerciseSet
    }

    private fun loadExercisesFromRawResource(context: Context): HashSet<Exercise> {
        val resourceId = R.raw.workouts
        Log.d("LocalDB", "Attempting to load data from raw resource with ID: $resourceId")

        return try {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Log.d("LocalDB", "Successfully read raw resource file.")

            val exercisesList = Json.decodeFromString<List<Exercise>>(jsonString)
            Log.d("LocalDB", "Successfully parsed JSON into ${exercisesList.size} Exercise objects.")

            HashSet(exercisesList)
        } catch (e: Exception) {
            Log.e("LocalDB", "Failed to read or parse raw resource. Check file name and JSON syntax.", e)
            hashSetOf()
        }
    }
}