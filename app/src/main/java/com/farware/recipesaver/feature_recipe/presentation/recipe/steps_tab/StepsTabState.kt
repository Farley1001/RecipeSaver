package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus
import kotlinx.coroutines.flow.emptyFlow

data class StepsTabState(
    val steps: List<Step?> = emptyList(),
    val stepsFocus: List<StepFocus> = emptyList(),
)
