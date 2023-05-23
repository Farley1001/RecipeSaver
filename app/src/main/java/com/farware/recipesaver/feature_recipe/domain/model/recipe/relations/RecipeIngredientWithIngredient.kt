package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

data class RecipeIngredientWithIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Long,
    val measureId: Long,
    val amount: String,
    val ingredient: String,
    val type: String
)
