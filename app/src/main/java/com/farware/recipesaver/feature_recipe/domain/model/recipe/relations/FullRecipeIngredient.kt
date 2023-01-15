package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient

data class FullRecipeIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    var ingredientId: Long,
    var measureId: Int,
    var amount: String,
    var measure: String,
    var ingredient: String,
    val type: String
) {
    companion object {
        fun new(): FullRecipeIngredient {
            return FullRecipeIngredient(
                recipeIngredientId = null,
                recipeId = -1L,
                ingredientId = -1L,
                measureId = -1,
                amount = "",
                measure = "",
                ingredient = "",
                type = ""
            )
        }
    }
    fun toRecipeIngredient(): RecipeIngredient {
        return RecipeIngredient(
            recipeIngredientId,
            recipeId,
            ingredientId,
            measureId,
            amount,
            measure,
            ingredient
        )
    }

    fun toIngredient(): Ingredient {
        return Ingredient(
            ingredientId,
            ingredient,
            type
        )
    }
}
