package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    private val stateHandle  = mutableStateOf(SavedStateHandle)

    private var _state =  mutableStateOf(StepsTabState())
    val state: State<StepsTabState> = _state

    private var _newStep = mutableStateOf(Step(-1L, -1L, 0, null, ""))
    val newStep: State<Step> = _newStep

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
            is StepsTabEvent.SaveStep -> {
                viewModelScope.launch {
                    stepUseCases.addStep(event.step)
                }
            }
            is StepsTabEvent.DeleteStep -> {
                viewModelScope.launch {
                    stepUseCases.deleteStep(event.step)
                }
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

                _newStep.value = newStep(recipeId, state.value.steps.size + 1)

            }.launchIn(viewModelScope)
    }

    private fun newStep(recipeId: Long, stepNumber: Int): Step {
        return Step(
            stepId = -1,
            recipeId = recipeId ,
            stepNumber = stepNumber,
            text = "",
        )
    }
}