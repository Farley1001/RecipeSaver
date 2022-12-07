package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.AddRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.DeleteRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.GetRecipeIngredientsByRecipeIdUseCase

data class RecipeIngredientUseCases(
    val getRecipeIngredientsByRecipeId: GetRecipeIngredientsByRecipeIdUseCase,
    val addRecipeIngredient: AddRecipeIngredientUseCase,
    val deleteRecipeIngredient: DeleteRecipeIngredientUseCase
)
