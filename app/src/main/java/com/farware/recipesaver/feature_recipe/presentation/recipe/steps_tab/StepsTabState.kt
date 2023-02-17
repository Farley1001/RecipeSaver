package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus
import kotlinx.coroutines.flow.emptyFlow

data class StepsTabState(
    val steps: List<Step?> = emptyList(),
    val stepsFocus: List<StepFocus> = emptyList(),
    val editedStep: Step = Step.new(),
    val stepToDelete: Step = Step.new(),
    val editStepText: String = "",
    val newStepText: String = "",
    val showEditStepDialog: Boolean = false,
    val showDeleteStepDialog: Boolean = false,
    val showNewStepDialog: Boolean = false
)
