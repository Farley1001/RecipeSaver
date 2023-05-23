package com.farware.recipesaver.feature_recipe.domain.model.recipe

import java.math.RoundingMode

data class RecipeIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Long,
    val measureId: Long,
    val amount: String
) {
    /**
     * create any functions that are needed to operate on this entity
     */


    /**
     * override toString() to return amount as the default
     */
    override fun toString() = "$amount"
}

class InvalidRecipeIngredientException(message: String): Exception(message)
