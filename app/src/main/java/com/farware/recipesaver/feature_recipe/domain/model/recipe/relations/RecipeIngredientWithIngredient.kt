package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

data class RecipeIngredientWithIngredient(
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Long,
    val measureId: Int,
    val amount: String,
    val measure: String,
    val ingredient: String,
    val ingredientName: String,
    val type: String
)
