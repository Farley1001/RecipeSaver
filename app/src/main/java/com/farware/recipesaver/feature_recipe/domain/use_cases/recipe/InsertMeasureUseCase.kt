package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidConversionException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidMeasureException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertMeasureUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidMeasureException::class)
    suspend operator fun invoke(measure: Measure) {
        if(measure.name.isBlank()) {
            throw InvalidMeasureException("The name of the measure can't be empty.")
        }
        if(measure.measureId == -1L) {
            throw InvalidConversionException("The measureId has not been set.")
        }
        repository.insertMeasure(measure)
    }
}