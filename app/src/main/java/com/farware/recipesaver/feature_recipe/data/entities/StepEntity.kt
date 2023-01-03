package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step

@Entity(
    tableName = "step_table",
    foreignKeys = [
        ForeignKey(entity = RecipeEntity::class, parentColumns = ["recipeId"], childColumns = ["recipeId"])
    ],
    indices = [Index("recipeId")]
)
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    val stepId: Long? = null,
    val recipeId: Long,
    val stepNumber: Int,
    val imagePath: String? = null,
    val text: String
) {
    companion object {
        fun from(step: Step): StepEntity {
            return StepEntity(
                step.stepId,
            step.recipeId,
            step.stepNumber,
            step.imagePath,
            step.text
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toStep(): Step {
        return Step(
            stepId,
            recipeId,
            stepNumber,
            imagePath,
            text
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = text
}

class InvalidStepEntityException(message: String): Exception(message)