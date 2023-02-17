package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus

sealed class StepsTabEvent {
    data class StepFocusChanged(val stepFocus: StepFocus) : StepsTabEvent()
    data class DeleteStep(val step: StepFocus): StepsTabEvent()
    object CancelConfirmDeleteStep: StepsTabEvent()
    object ConfirmDeleteStep: StepsTabEvent()
    data class EditStep(val step: StepFocus): StepsTabEvent()
    data class EditStepTextChanged(val stepText: String): StepsTabEvent()
    object SaveEditStep: StepsTabEvent()
    object CancelEditStep: StepsTabEvent()
    object ToggleNewStepDialog: StepsTabEvent()
    data class NewStepTextChanged(val stepText: String): StepsTabEvent()
    object SaveNewStep: StepsTabEvent()

}