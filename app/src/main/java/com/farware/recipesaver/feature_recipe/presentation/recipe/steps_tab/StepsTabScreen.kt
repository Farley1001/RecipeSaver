package com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.*
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@Composable
fun StepsTabScreen(
    viewModel: StepsTabViewModel = hiltViewModel(),
) {
    StepsTabContent(
        steps = viewModel.state.value.stepsFocus,
        newStep = viewModel.newStep.value,
        stepFocusChanged = { viewModel.onEvent(StepsTabEvent.StepFocusChanged(it)) },
        saveStepClicked = { viewModel.onEvent(StepsTabEvent.SaveStep(it)) },
        deleteStepClicked = { viewModel.onEvent(StepsTabEvent.DeleteStep(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsTabContent(
    steps: List<StepFocus>,
    newStep: Step,
    stepFocusChanged: (StepFocus) -> Unit,
    saveStepClicked: (Step) -> Unit,
    deleteStepClicked: (Step) -> Unit
) {
    val showNewStepDialog = remember { mutableStateOf(false)}
    val text = remember { mutableStateOf("") }


    if(showNewStepDialog.value) {
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showNewStepDialog.value = false
            },
            title = {
                Text(text = "Edit Step")
            },
            text = {
                TextField(
                    modifier = Modifier.fillMaxHeight(0.5F),
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNewStepDialog.value = false
                        saveStepClicked(
                            newStep.copy(
                                text = text.value
                            )
                        )
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNewStepDialog.value = false
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
                            step = step,
                            focused = step.focused,
                            onChangeFocus = { stepFocusChanged(it) },
                            onSaveClicked = { saveStepClicked(it) },
                            onDeleteClicked = { deleteStepClicked(it) },
                        )
                    }
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { showNewStepDialog.value = true },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new step") },
                text = { Text(text = "New Step") },
            )
        }
    }
}