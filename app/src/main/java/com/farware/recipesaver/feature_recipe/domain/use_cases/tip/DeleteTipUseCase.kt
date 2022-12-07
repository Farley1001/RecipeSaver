package com.farware.recipesaver.feature_recipe.domain.use_cases.tip

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class DeleteTipUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(tip: Tip) {
        repository.deleteTip(tip)
    }
}