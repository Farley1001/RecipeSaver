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
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipTextWithIcon(
    tip: TipFocus,
    focused: Boolean,
    onChangeFocus: (TipFocus) -> Unit,
    onSaveClicked: (Tip) -> Unit,
    onDeleteClicked: (Tip) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    val isFocused = remember { mutableStateOf(false) }
    val isEditMode = remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(true) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }
    text.value = tip.tip.text

    if(!focused) {
        isFocused.value = false
        isEditMode.value = false
    } else {
        isFocused.value = true
    }

    Column(
        Modifier
            //.fillMaxSize()
            .wrapContentSize()
    ) {
        if(isEditMode.value) {
            AlertDialog(
                modifier = Modifier
                    .customDialogPosition(CustomDialogPosition.TOP)
                    .padding(top = 20.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openEditDialog.value = false
                    isEditMode.value = false
                },
                title = {
                    Text(text = "Edit Tip")
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
                            openEditDialog.value = false
                            isEditMode.value = false
                            tip.copy(
                                tip = tip.tip.copy(
                                    text = text.value
                                )
                            )
                            onSaveClicked(tip.tip.copy(
                                text = text.value
                            ))
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openEditDialog.value = false
                            isEditMode.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        } else {
            TextWithAppendedContent(
                text = tip.tip.text,
                onTextClicked = { onChangeFocus(tip) },
                placeholderWidth = 84.sp,
                placeholderHeight = 24.sp,
                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                appendContent = {
                    if(isFocused.value) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Local Description",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isEditMode.value = true
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
                                    isFocused.value = false
                                    openConfirmDeleteDialog.value = true
                                }
                        )
                    }
                }
            )
            if(openConfirmDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onDismissRequest.
                        openConfirmDeleteDialog.value = false
                    },
                    title = {
                        Text(text = "Confirm Delete Tip")
                    },
                    text = {
                        Text(text = "Are you sure you want to delete this tip?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openConfirmDeleteDialog.value = false
                                onDeleteClicked(tip.tip)
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openConfirmDeleteDialog.value = false
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