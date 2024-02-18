package com.example.core.exercise.application.`in`

import com.example.core.exercise.application.`in`.command.SaveExerciseAreaCommand
import com.example.domain.exercise.ExerciseArea

interface ExerciseAreaUseCase {
    fun saveExerciseArea(command: SaveExerciseAreaCommand)

    fun deleteExerciseArea(id: Long)

    fun getExerciseArea(id: Long): ExerciseArea

    fun getAllExerciseArea(): List<ExerciseArea>
}
