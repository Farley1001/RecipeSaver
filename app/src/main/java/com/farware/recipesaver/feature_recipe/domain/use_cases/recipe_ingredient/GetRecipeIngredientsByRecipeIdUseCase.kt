package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipeIngredientsByRecipeIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: Long): Flow<List<FullRecipeIngredient?>> {
        return repository.getRecipeIngredientsByRecipeId(id)
    }
}