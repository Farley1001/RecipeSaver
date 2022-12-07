package com.farware.recipesaver.feature_recipe.domain.use_cases.tip

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetTipsByRecipeIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: Long): Flow<List<Tip?>> {
        return repository.getTipsByRecipeId(id)
    }
}