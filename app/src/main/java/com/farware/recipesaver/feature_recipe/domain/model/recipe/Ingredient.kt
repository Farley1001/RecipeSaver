package com.farware.recipesaver.feature_recipe.domain.model.recipe

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * This entity contains the ingredients selected from a master list on firebase
 * and used in a local recipe.
*/
@Entity(
    tableName = "ingredient",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = false)
    val ingredientId: Int,
    val name: String,
    val type: String
) {
    /**
     * create any functions that are needed to operate on this entity
     */
    /*fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }*/

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidIngredientException(message: String): Exception(message)
