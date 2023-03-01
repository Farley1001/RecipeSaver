package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidConversionException
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertConversionUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidConversionException::class)
    suspend operator fun invoke(conversion: Conversion) {
        if(conversion.name.isBlank()) {
            throw InvalidConversionException("The name of the conversion can't be empty.")
        }
        if(conversion.shortName.isBlank()) {
            throw InvalidConversionException("The short Name of the measure can't be empty.")
        }
        if(conversion.conversionId == -1L) {
            throw InvalidConversionException("The conversionId has not been set.")
        }
        if(conversion.sizeText.isBlank()) {
            throw InvalidConversionException("The size must be a number.")
        }
        if(conversion.size.isNaN()) {
            throw InvalidConversionException("The Fluid Ounces US must be a number.")
        }
        if(conversion.flozUs.isNaN()) {
            throw InvalidConversionException("The Fluid Ounces US must be a number.")
        }
        if(conversion.flozImp.isNaN()) {
            throw InvalidConversionException("The Fluid Ounces Imperial must be a number.")
        }
        if(conversion.mlUs.isNaN()) {
            throw InvalidConversionException("The Milliliters US must be a number.")
        }
        if(conversion.mlImp.isNaN()) {
            throw InvalidConversionException("The Milliliters Imperial must be a number.")
        }
        repository.insertConversion(conversion)
    }
}