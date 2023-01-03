package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient

data class FullRecipeIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Int,
    val measureId: Int,
    val amount: String,
    val measure: String,
    val ingredient: String,
    val ingredientName: String,
    val type: String
) {
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
}
