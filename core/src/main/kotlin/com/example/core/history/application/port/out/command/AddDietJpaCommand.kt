package com.example.core.history.application.port.out.command

import com.example.domain.history.Diet
import java.util.UUID

data class AddDietJpaCommand(
    val todayHistoryId: UUID,
    val imageIds: List<Long>?,
    val foods: List<String>?,
    val type: Diet,
)
