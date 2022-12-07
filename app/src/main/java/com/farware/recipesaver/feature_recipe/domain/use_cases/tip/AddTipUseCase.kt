package com.farware.recipesaver.feature_recipe.domain.use_cases.tip

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidTipException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class AddTipUseCase(
    private val repository: RecipeRepository
) {
    @Throws(InvalidTipException::class)
    suspend operator fun invoke(tip: Tip) {
        if(tip.tipNumber < 1) {
            throw InvalidTipException("The tip number was not supplied.")
        }
        if(tip.recipeId < 1) {
            throw InvalidTipException("The recipeId was not supplied.")
        }
        if(tip.text.isBlank()){
            throw InvalidTipException("The tip text was not supplied.")
        }
        repository.insertTip(tip)
    }
}