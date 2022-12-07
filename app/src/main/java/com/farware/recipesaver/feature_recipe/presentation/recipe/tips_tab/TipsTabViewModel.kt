package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.domain.use_cases.TipUseCases
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TipsTabViewModel @Inject constructor(
    private val tipsUseCases: TipUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var _state =  mutableStateOf(TipsTabState())
    val state: State<TipsTabState> = _state

    private var _newTip = mutableStateOf(Tip(-1L, -1L, 0, null, ""))
    val newTip: State<Tip> = _newTip

    private var getTipsJob: Job? = null

    init {
        savedStateHandle.get<Long>("recipeId")?.let { recipeId ->
            if(recipeId != -1L) {
                getTips(recipeId)
            }
        }
    }

    fun onEvent(event: TipsTabEvent) {
        when (event) {
            is TipsTabEvent.TipFocusChanged -> {
                var tf = state.value.tipsFocus
                tf = tf.map { item ->
                    if(item.tip.tipId == event.tipFocus.tip.tipId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    tipsFocus = tf
                )
            }
            is TipsTabEvent.SaveTip -> {
                viewModelScope.launch {
                    tipsUseCases.addTip(event.tip)
                }
            }
            is TipsTabEvent.DeleteTip -> {
                viewModelScope.launch {
                    tipsUseCases.deleteTip(event.tip)
                }
            }
        }
    }

    private fun getTips(recipeId: Long) {
        getTipsJob?.cancel()
        getTipsJob = tipsUseCases.getTipsByRecipeId(recipeId)
            .onEach { tips ->
                _state.value = state.value.copy(
                    tips = tips
                )
                var tfList = listOf<TipFocus>()
                tips.forEach { tip ->
                    tfList += TipFocus(
                        tip = tip!!,
                        focused = false
                    )
                }
                _state.value = _state.value.copy(
                    tipsFocus = tfList
                )

                _newTip.value = newTip(recipeId, state.value.tips.size + 1)
            }
            .launchIn(viewModelScope)
    }

    private fun newTip(recipeId: Long, tipNumber: Int): Tip {
        return Tip(
            tipId = -1,
            recipeId = recipeId ,
            tipNumber = tipNumber,
            text = "",
        )
    }
}