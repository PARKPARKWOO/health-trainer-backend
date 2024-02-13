package com.example.core.exercise.adapter.out

import com.example.core.common.error.ErrorCode
import com.example.core.common.error.ServiceException
import com.example.core.exercise.adapter.out.persistence.entity.ExerciseAreaEntity
import com.example.core.exercise.adapter.out.persistence.repository.ExerciseAreaRepository
import com.example.core.exercise.application.`in`.command.SaveExerciseAreaCommand
import com.example.core.exercise.application.out.ExerciseAreaJpaPort
import com.example.domain.exercise.ExerciseArea
import org.springframework.stereotype.Component

@Component
class ExerciseAreaJpaAdapter(
    private val exerciseAreaRepository: ExerciseAreaRepository,
) : ExerciseAreaJpaPort {
    override fun saveExerciseArea(command: SaveExerciseAreaCommand) {
        val entity = ExerciseAreaEntity(
            area = command.area,
        )
        exerciseAreaRepository.save(entity)
    }

    override fun getExerciseArea(id: Long): ExerciseArea {
        return getEntity(id).toDomain()
    }

    override fun getExerciseAreas(ids: List<Long>): List<ExerciseArea>? {
        return exerciseAreaRepository.queryIdsIn(ids)?.map { entity ->
            entity.toDomain()
        }
    }

    override fun delete(id: Long) {
        val entity = getEntity(id)
        exerciseAreaRepository.delete(entity)
    }

    private fun getEntity(id: Long): ExerciseAreaEntity {
        return exerciseAreaRepository.findById(id).orElseThrow {
            throw ServiceException(ErrorCode.NOT_FOUND_EXERCISE_AREA)
        }
    }
}