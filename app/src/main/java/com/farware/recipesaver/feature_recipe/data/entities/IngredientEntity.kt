package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient

/*
 * This entity contains the ingredients selected from a master list on firebase
 * and used in a local recipe.
*/
@Entity(
    tableName = "ingredient_table",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long? = null,
    val name: String,
    val type: String
) {
    companion object {
        fun from(ingredient: Ingredient): IngredientEntity {
            return IngredientEntity(
                ingredient.ingredientId,
                ingredient.name,
                ingredient.type
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toIngredient(): Ingredient {
        return Ingredient(
            ingredientId,
            name,
            type
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidIngredientEntityException(message: String): Exception(message)