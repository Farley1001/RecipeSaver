package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidConversionException
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertAllConversionsUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidConversionException::class)
    suspend operator fun invoke(conversions: List<Conversion>) {
        if (conversions.isEmpty()) {
            throw InvalidConversionException("The conversion list is empty.")
        }
        repository.insertAllConversions(conversions)
    }
}