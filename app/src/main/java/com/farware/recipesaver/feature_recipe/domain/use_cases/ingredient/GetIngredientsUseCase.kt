package com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetIngredientsUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Ingredient>> {
        return repository.getIngredients()
    }
}