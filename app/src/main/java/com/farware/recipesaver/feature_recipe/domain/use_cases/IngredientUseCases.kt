package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientByIdUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientByNameUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.GetIngredientsUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient.InsertIngredientReturnIdUseCase

data class IngredientUseCases(
    val getIngredients: GetIngredientsUseCase,
    val getIngredientById: GetIngredientByIdUseCase,
    val getIngredientByName: GetIngredientByNameUseCase,
    val insertIngredientReturnId: InsertIngredientReturnIdUseCase,
)