package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidMeasureException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertAllMeasuresUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidMeasureException::class)
    suspend operator fun invoke(measures: List<Measure>) {
        if (measures.isEmpty()) {
            throw InvalidMeasureException("The measure list is empty.")
        }
        repository.insertAllMeasures(measures)
    }
}