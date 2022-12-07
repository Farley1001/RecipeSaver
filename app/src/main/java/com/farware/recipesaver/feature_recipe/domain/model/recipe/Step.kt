package com.farware.recipesaver.feature_recipe.domain.model.recipe

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "step",
    foreignKeys = [
        ForeignKey(entity = Recipe::class, parentColumns = ["recipeId"], childColumns = ["recipeId"])
    ],
    indices = [Index("recipeId")]
)
data class Step(
    @PrimaryKey(autoGenerate = true)
    val stepId: Long? = null,
    val recipeId: Long,
    val stepNumber: Int,
    val imagePath: String? = null,
    val text: String
) {
    /**
     * create any functions that are needed to operate on this entity
     */
    /*fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }*/

    /**
     * override toString() to return name as the default
     */
    override fun toString() = text
}

class InvalidStepException(message: String): Exception(message)
