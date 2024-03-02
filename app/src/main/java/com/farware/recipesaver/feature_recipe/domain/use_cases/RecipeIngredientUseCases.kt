package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.InsertRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.DeleteRecipeIngredientUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient.GetRecipeIngredientsByRecipeIdUseCase

data class RecipeIngredientUseCases(
    val getRecipeIngredientsByRecipeId: GetRecipeIngredientsByRecipeIdUseCase,
    val insertRecipeIngredient: InsertRecipeIngredientUseCase,
    val deleteRecipeIngredient: DeleteRecipeIngredientUseCase
)
