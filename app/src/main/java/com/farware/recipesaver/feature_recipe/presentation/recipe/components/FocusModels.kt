package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient

data class StepFocus(
    val step: Step,
    var focused: Boolean
)

data class IngredientFocus(
    val fullIngredient: FullRecipeIngredient,
    val measureAmount: RecipeIngredient,
    val measure: Measure,
    val ingredient: Ingredient,
    var focused: Boolean
)

data class TipFocus(
    val tip: Tip,
    var focused: Boolean
)