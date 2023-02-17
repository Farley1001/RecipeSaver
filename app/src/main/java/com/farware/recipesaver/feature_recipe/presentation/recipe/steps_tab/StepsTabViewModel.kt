package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.use_cases.StepUseCases
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepsTabViewModel @Inject constructor(
    private val stepUseCases: StepUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _state =  mutableStateOf(StepsTabState())
    val state: State<StepsTabState> = _state

    private var _newStep = mutableStateOf(Step.new())
    private val newStep: State<Step> = _newStep

    private var getStepsJob: Job? = null

    init {
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if (recipeId != -1L) {
                getSteps(recipeId)
            }
        }

    }

    fun onEvent(event: StepsTabEvent) {
        when (event) {
            is StepsTabEvent.StepFocusChanged -> {
                var sf = state.value.stepsFocus
                sf = sf.map { item ->
                    if(item.step.stepId == event.stepFocus.step.stepId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    stepsFocus = sf
                )
            }
            is StepsTabEvent.DeleteStep -> {
                var sf = state.value.stepsFocus
                sf = sf.map { item ->
                    if(item.step.stepId == event.step.step.stepId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }
                _state.value = state.value.copy(
                    showDeleteStepDialog = true,
                    stepToDelete = event.step.step
                )
            }
            is StepsTabEvent.CancelConfirmDeleteStep -> {
                _state.value = state.value.copy(
                    showDeleteStepDialog = false
                )
            }
            is StepsTabEvent.ConfirmDeleteStep -> {
                _state.value = state.value.copy(
                    showDeleteStepDialog = false
                )
                viewModelScope.launch {
                    stepUseCases.deleteStep(state.value.stepToDelete)
                }
            }
            is StepsTabEvent.EditStep -> {
                var sf = state.value.stepsFocus
                sf = sf.map { item ->
                    if(item.step.stepId == event.step.step.stepId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }
                _state.value = state.value.copy(
                    showEditStepDialog = true,
                    stepsFocus = sf,
                    editedStep = event.step.step,
                    editStepText = event.step.step.text
                )
            }
            is StepsTabEvent.EditStepTextChanged -> {
                _state.value = state.value.copy(
                    editStepText = event.stepText
                )
            }
            is StepsTabEvent.SaveEditStep -> {
                // update the step with edited text
                var step = state.value.editedStep
                step = step.copy(
                    text = state.value.editStepText
                )
                // save the step
                viewModelScope.launch {
                    stepUseCases.addStep(step)
                }
                // hide the edit dialog
                _state.value = _state.value.copy(
                    showEditStepDialog = false,
                    editStepText = ""
                )
            }
            is StepsTabEvent.CancelEditStep -> {
                _state.value = state.value.copy(
                    showEditStepDialog = false,
                    editStepText = ""
                )
            }
            is StepsTabEvent.ToggleNewStepDialog -> {
                _state.value = state.value.copy(
                    showNewStepDialog = !state.value.showNewStepDialog,
                    newStepText = ""
                )
            }
            is StepsTabEvent.NewStepTextChanged -> {
                _state.value = state.value.copy(
                    newStepText = event.stepText
                )
            }
            is StepsTabEvent.SaveNewStep -> {
                _newStep.value = newStep.value.copy(
                    text = state.value.newStepText
                )
                viewModelScope.launch {
                    stepUseCases.addStep(newStep.value)
                }
                _newStep.value = newStep.value.copy(
                    stepNumber = state.value.steps.size + 1,
                )
                _state.value = state.value.copy(
                    showNewStepDialog = !state.value.showNewStepDialog,
                    newStepText = ""
                )
            }
        }
    }

    private fun getSteps(recipeId: Long) {
        getStepsJob?.cancel()
        getStepsJob = stepUseCases.getStepsByRecipeId(recipeId)
            .onEach { steps ->
                _state.value = state.value.copy(
                    steps = steps
                )
                var sfList = listOf<StepFocus>()
                steps.forEach { step ->
                    sfList += StepFocus(
                        step = step!!,
                        focused = false
                    )
                }
                _state.value = _state.value.copy(
                    stepsFocus = sfList
                )

                _newStep.value = _newStep.value.copy(
                    recipeId = recipeId,
                    stepNumber = steps.size + 1
                )

            }.launchIn(viewModelScope)
    }
}