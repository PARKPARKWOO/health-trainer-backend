package com.example.core.exercise.adapter.out.persistence.entity.relationship

import com.example.core.exercise.adapter.out.persistence.entity.EXERCISE_GOAL_ID
import com.example.core.exercise.adapter.out.persistence.entity.EXERCISE_ITEM_ID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

const val ITEM_GOAL_RELATIONSHIP_TABLE_NAME = "item_goal"

@Entity
@Table(name = ITEM_GOAL_RELATIONSHIP_TABLE_NAME)
class ItemGoalRelationshipEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    @Column(name = EXERCISE_ITEM_ID)
    var exerciseItemId: Long,
    @Column(name = EXERCISE_GOAL_ID)
    var exerciseGoalId: Long,
)
