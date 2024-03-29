package com.example.core.history.application.service

import com.example.core.history.application.port.`in`.GenerateHistoryUseCase
import com.example.core.history.application.port.`in`.HistoryQueryUseCase
import com.example.core.history.application.port.`in`.command.AddDietJpaCommand
import com.example.core.history.application.port.`in`.command.ExerciseTodayCommand
import com.example.core.history.application.port.out.ExerciseHistoryJpaPort
import com.example.core.history.application.port.out.UserHistoryJpaPort
import com.example.core.history.application.port.out.command.AttendanceTodayCommand
import com.example.core.history.application.port.out.command.SaveExerciseHistoryCommand
import com.example.core.history.application.port.out.query.FindUserHistoryQuery
import com.example.core.history.dto.HistoryResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.UUID

@Service
class HistoryService(
    private val userHistoryJpaPort: UserHistoryJpaPort,
    private val exerciseHistoryJpaPort: ExerciseHistoryJpaPort,
) : GenerateHistoryUseCase, HistoryQueryUseCase {
    @Transactional
    override fun exerciseToday(command: ExerciseTodayCommand) {
        exerciseHistoryJpaPort.saveAll(
            command.items.map {
                SaveExerciseHistoryCommand(
                    itemId = it.itemId,
                    weight = it.weight,
                    exerciseSet = it.exerciseSet,
                    userHistoryId = command.userHistoryId,
                )
            },
        )
    }

    @Transactional
    override fun attendanceToday(userId: UUID): UUID {
        val attendanceTodayCommand = AttendanceTodayCommand(
            userId = userId,
            today = LocalDate.now(),
        )
        return userHistoryJpaPort.attendanceToday(attendanceTodayCommand)
    }

    @Transactional
    override fun addDietCommand(command: AddDietJpaCommand) {
        command.images?.let {
            userHistoryJpaPort.addImage(command.toImageCommand())
        }
        command.foods?.let {
            userHistoryJpaPort.addFood(command.toFoodCommand())
        }
    }

    /**
     * 주의 시작은 월요일,
     * 주의 마지막은 일요일
     */
    private fun getStartOfWeek(today: LocalDate) =
        today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private fun getEndOfWeek(today: LocalDate) =
        today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    private fun getStartOfMonth(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.firstDayOfMonth())

    private fun getEndOfMonth(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.lastDayOfMonth())

    @Transactional(readOnly = true)
    override fun getHistoryFromWeek(userId: UUID): List<HistoryResponseDto>? {
        val today = LocalDate.now()
        val query = FindUserHistoryQuery(
            userId = userId,
            startDate = getStartOfWeek(today),
            endDate = getEndOfWeek(today),
        )
        return userHistoryJpaPort.queryUserHistory(query)
    }

    @Transactional(readOnly = true)
    override fun getHistoryFromMonth(userId: UUID): List<HistoryResponseDto>? {
        val today = LocalDate.now()
        val query = FindUserHistoryQuery(
            userId = userId,
            startDate = getStartOfMonth(today),
            endDate = getEndOfMonth(today),
        )
        return userHistoryJpaPort.queryUserHistory(query)
    }

    @Transactional(readOnly = true)
    override fun getHistoryFromToday(userId: UUID): HistoryResponseDto? {
        val today = LocalDate.now()
        val query = FindUserHistoryQuery(
            userId = userId,
            startDate = today,
            endDate = today,
        )
        return userHistoryJpaPort.queryUserHistory(query)?.get(0)
    }
}
