package com.farware.recipesaver.feature_recipe.domain.use_cases.measure

import androidx.lifecycle.LiveData
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidMeasureException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertMeasureReturnIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidMeasureException::class)
    suspend operator fun invoke(measure: Measure): Long {
        if (measure.name.isBlank()) {
            throw InvalidMeasureException("The measure must have a name.")
        }

        return repository.insertMeasureReturnId(measure)
    }
}