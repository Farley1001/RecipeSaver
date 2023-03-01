package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.*
import com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab.TipsTabEvent
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@Composable
fun StepsTabScreen(
    recipeId: Long = -1,
    viewModel: StepsTabViewModel = hiltViewModel(),
) {
    val recipeid = recipeId
    StepsTabContent(
        steps = viewModel.state.value.stepsFocus,
        newStepText = viewModel.state.value.newStepText,
        editStepText = viewModel.state.value.editStepText,
        showEditStepDialog = viewModel.state.value.showEditStepDialog,
        showConfirmDeleteStepDialog = viewModel.state.value.showDeleteStepDialog,
        showNewStepDialog = viewModel.state.value.showNewStepDialog,
        onEditStepTextChanged = { viewModel.onEvent(StepsTabEvent.EditStepTextChanged(it)) },
        onNewStepTextChanged = { viewModel.onEvent(StepsTabEvent.NewStepTextChanged(it)) },
        onSaveNewStepClicked = { viewModel.onEvent(StepsTabEvent.SaveNewStep) },
        onStepFocusChanged = { viewModel.onEvent(StepsTabEvent.StepFocusChanged(it)) },
        onSaveStepClicked = { viewModel.onEvent(StepsTabEvent.SaveEditStep) },
        onCancelEditStepClicked = { viewModel.onEvent(StepsTabEvent.CancelEditStep) },
        onEditStepClicked = { viewModel.onEvent(StepsTabEvent.EditStep(it))},
        onDeleteStepClicked = { viewModel.onEvent(StepsTabEvent.DeleteStep(it)) },
        onConfirmDeleteStepClicked = { viewModel.onEvent(StepsTabEvent.ConfirmDeleteStep) },
        onCancelConfirmDelete = { viewModel.onEvent(StepsTabEvent.CancelConfirmDeleteStep) },
        onToggleNewStepDialog = { viewModel.onEvent(StepsTabEvent.ToggleNewStepDialog) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsTabContent(
    steps: List<StepFocus?>,
    newStepText: String,
    editStepText: String,
    showEditStepDialog: Boolean,
    showConfirmDeleteStepDialog: Boolean,
    showNewStepDialog: Boolean,
    onEditStepTextChanged: (String) -> Unit,
    onNewStepTextChanged: (String) -> Unit,
    onSaveNewStepClicked: () -> Unit,
    onStepFocusChanged: (StepFocus) -> Unit,
    onSaveStepClicked: () -> Unit,
    onCancelEditStepClicked: () -> Unit,
    onEditStepClicked: (StepFocus) -> Unit,
    onDeleteStepClicked: (StepFocus) -> Unit,
    onConfirmDeleteStepClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit,
    onToggleNewStepDialog: () -> Unit
) {
    if(showNewStepDialog) {
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                onToggleNewStepDialog()
            },
            title = {
                Text(text = "New Step")
            },
            text = {
                TextField(
                    modifier = Modifier.fillMaxHeight(0.5F),
                    value = newStepText,
                    onValueChange = {
                        onNewStepTextChanged(it)
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveNewStepClicked()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onToggleNewStepDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                items(steps) { step ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        StepTextWithIcon(
                            step = step!!,
                            editStepText = editStepText,
                            isFocused = step.focused,
                            showEditStepDialog = showEditStepDialog,
                            showConfirmDeleteStepDialog = showConfirmDeleteStepDialog,
                            onStepTextChanged = { onEditStepTextChanged(it) },
                            onStepFocusChanged = { onStepFocusChanged(it) },
                            onSaveStepClicked = { onSaveStepClicked() },
                            onCancelEditStepClicked = { onCancelEditStepClicked() },
                            onEditStepClicked = { onEditStepClicked(it) },
                            onDeleteStepClicked = { onDeleteStepClicked(it) },
                            onConfirmDeleteStepClicked = { onConfirmDeleteStepClicked() },
                            onCancelConfirmDelete = { onCancelConfirmDelete() }
                        )
                    }
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { onToggleNewStepDialog() },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new step") },
                text = { Text(text = "New Step") },
            )
        }
    }
}