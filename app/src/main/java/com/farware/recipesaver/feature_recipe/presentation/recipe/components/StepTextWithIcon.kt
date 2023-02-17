package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Step
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTextWithIcon(
    step: StepFocus,
    editStepText: String,
    isFocused: Boolean,
    showEditStepDialog: Boolean,
    showConfirmDeleteStepDialog: Boolean,
    onStepTextChanged: (String) -> Unit,
    onStepFocusChanged: (StepFocus) -> Unit,
    onSaveStepClicked: () -> Unit,
    onCancelEditStepClicked: () -> Unit,
    onEditStepClicked: (StepFocus) -> Unit,
    onDeleteStepClicked: (StepFocus) -> Unit,
    onConfirmDeleteStepClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit
) {
    Column(
        Modifier
            //.fillMaxSize()
            .wrapContentSize()
    ) {
        if(showEditStepDialog) {
            AlertDialog(
                modifier = Modifier
                    .customDialogPosition(CustomDialogPosition.TOP)
                    .padding(top = 20.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    onCancelEditStepClicked()
                },
                title = {
                    Text(text = "Edit Step")
                },
                text = {
                    TextField(
                        modifier = Modifier.fillMaxHeight(0.5F),
                        value = editStepText,
                        onValueChange = {
                            onStepTextChanged(it)
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSaveStepClicked()
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onCancelEditStepClicked()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        } else {
            TextWithAppendedContent(
                text = "${step.step.stepNumber}.  ${step.step.text}",
                onTextClicked = { onStepFocusChanged(step) },
                placeholderWidth = 84.sp,
                placeholderHeight = 24.sp,
                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                appendContent = {
                    if(isFocused) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Local Description",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    onEditStepClicked(step)
                                }
                        )
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Local Description",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .size(24.dp)
                                .clickable {
                                    onDeleteStepClicked(step)
                                }
                        )
                    }
                }
            )
            if(showConfirmDeleteStepDialog) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onDismissRequest.
                        onCancelConfirmDelete()
                    },
                    title = {
                        Text(text = "Confirm Delete Step")
                    },
                    text = {
                        Text(text = "Are you sure you want to delete this step?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onConfirmDeleteStepClicked()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onCancelConfirmDelete()
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}