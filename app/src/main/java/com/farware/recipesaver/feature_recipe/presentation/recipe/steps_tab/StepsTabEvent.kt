package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus

sealed class StepsTabEvent {
    data class StepFocusChanged(val stepFocus: StepFocus) : StepsTabEvent()
    data class SaveStep(val step: Step): StepsTabEvent()
    data class DeleteStep(val step: Step): StepsTabEvent()
}