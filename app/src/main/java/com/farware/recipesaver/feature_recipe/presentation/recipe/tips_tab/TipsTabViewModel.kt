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

    private var _newTip = mutableStateOf(Tip.new())
    private val newTip: State<Tip> = _newTip

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
                var tf = state.value.tipFocus
                tf = tf.map { item ->
                    if(item.tip.tipId == event.tipFocus.tip.tipId)
                        item.copy(focused = true)
                    else
                        item.copy(focused = false)
                }
                _state.value = state.value.copy(
                    tipFocus = tf
                )
            }
            is TipsTabEvent.DeleteTip -> {
                var tf = state.value.tipFocus
                tf = tf.map { item ->
                    if(item.tip.tipId == event.tip.tip.tipId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }
                _state.value = state.value.copy(
                    showDeleteTipDialog = true,
                    tipToDelete = event.tip.tip
                )
            }
            is TipsTabEvent.CancelConfirmDeleteTip -> {
                _state.value = state.value.copy(
                    showDeleteTipDialog = false
                )
            }
            is TipsTabEvent.ConfirmDeleteTip -> {
                _state.value = state.value.copy(
                    showDeleteTipDialog = false
                )
                viewModelScope.launch {
                    tipsUseCases.deleteTip(state.value.tipToDelete)
                }
            }
            is  TipsTabEvent.EditTip -> {
                var tf = state.value.tipFocus
                tf = tf.map { item ->
                    if(item.tip.tipId == event.tip.tip.tipId) {
                        item.copy(
                            focused = true
                        )
                    }
                    else {
                        item.copy(focused = false)
                    }
                }
                _state.value = state.value.copy(
                    showEditTipDialog = true,
                    tipFocus = tf,
                    editedTip = event.tip.tip,
                    editTipText = event.tip.tip.text
                )
            }
            is TipsTabEvent.EditTipTextChanged -> {
                _state.value = state.value.copy(
                    editTipText = event.tipText
                )
            }
            is TipsTabEvent.SaveEditTip -> {
                // update the tip with edited text
                var tip = state.value.editedTip
                tip = tip.copy(
                    text = state.value.editTipText
                )
                // save the tip
                viewModelScope.launch {
                    tipsUseCases.addTip(tip)
                }
                // hide the edit dialog
                _state.value = _state.value.copy(
                    showEditTipDialog = false,
                    editTipText = ""
                )
            }
            is TipsTabEvent.CancelEditTip -> {
                _state.value = state.value.copy(
                    showEditTipDialog = false,
                    editTipText = ""
                )
            }
            is TipsTabEvent.ToggleNewTipDialog -> {
                _state.value = state.value.copy(
                    showNewTipDialog = !state.value.showNewTipDialog,
                    newTipText = ""
                )
            }
            is TipsTabEvent.NewTipTextChanged -> {
                _state.value = state.value.copy(
                    newTipText = event.tipText
                )
            }
            is TipsTabEvent.SaveNewTip -> {
                // update tip with new text
                _newTip.value = newTip.value.copy(
                    text = state.value.newTipText
                )
                // save the new tip
                viewModelScope.launch {
                    tipsUseCases.addTip(newTip.value)
                }
                // update for the next newTip
                _newTip.value = newTip.value.copy(
                    tipNumber = state.value.tips.size + 1,
                )
                // close the dialog and clear state
                _state.value = state.value.copy(
                    showNewTipDialog = !state.value.showNewTipDialog,
                    newTipText = ""
                )
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
                    tipFocus = tfList
                )
                _newTip.value = _newTip.value.copy(
                    recipeId = recipeId,
                    tipNumber = tips.size + 1
                )
            }
            .launchIn(viewModelScope)
    }
}