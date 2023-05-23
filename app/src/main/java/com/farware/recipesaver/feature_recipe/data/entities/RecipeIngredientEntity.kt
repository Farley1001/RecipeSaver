package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import java.math.RoundingMode

@Entity(
    tableName = "recipe_ingredient_table",
    foreignKeys = [
        ForeignKey(entity = RecipeEntity::class, parentColumns = ["recipeId"], childColumns = ["recipeId"]),
        ForeignKey(entity = IngredientEntity::class, parentColumns = ["ingredientId"], childColumns = ["ingredientId"], onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(entity = MeasureEntity::class, parentColumns = ["measureId"], childColumns = ["measureId"], onDelete = ForeignKey.SET_DEFAULT)
    ],
    indices = [
        Index(value = ["recipeId"], unique = false),
        Index(value = ["ingredientId"], unique = false),
        Index(value = ["measureId"], unique = false)
    ]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Long,
    val measureId: Long,
    val amount: String
) {
    companion object {
        fun from(recipeIngredient: RecipeIngredient): RecipeIngredientEntity {
            return RecipeIngredientEntity(
                recipeIngredient.recipeIngredientId,
                recipeIngredient.recipeId,
                recipeIngredient.ingredientId,
                recipeIngredient.measureId,
                recipeIngredient.amount
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipeIngredient(): RecipeIngredient {
        return RecipeIngredient(
            recipeIngredientId,
            recipeId,
            ingredientId,
            measureId,
            amount
        )
    }

    /*
     * override toString() to return amount as the default
     */
    override fun toString() = "$amount"
}

class InvalidRecipeIngredientEntityException(message: String): Exception(message)