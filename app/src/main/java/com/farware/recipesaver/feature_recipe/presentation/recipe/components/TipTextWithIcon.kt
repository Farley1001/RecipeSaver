package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    editTipText: String,
    isFocused: Boolean,
    showEditTipDialog: Boolean,
    showConfirmDeleteTipDialog: Boolean,
    onTipTextChanged: (String) -> Unit,
    onTipFocusChanged: (TipFocus) -> Unit,
    onSaveTipClicked: () -> Unit,
    onCancelEditTipClicked: () -> Unit,
    onEditTipClicked: (TipFocus) -> Unit,
    onDeleteTipClicked: (TipFocus) -> Unit,
    onConfirmDeleteTipClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit
) {
    Column(
        Modifier
            //.fillMaxSize()
            .wrapContentSize()
    ) {
        if(showEditTipDialog) {
            AlertDialog(
                modifier = Modifier
                    .customDialogPosition(CustomDialogPosition.TOP)
                    .padding(top = 20.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    onCancelEditTipClicked()
                },
                title = {
                    Text(text = "Edit Tip")
                },
                text = {
                    TextField(
                        modifier = Modifier.fillMaxHeight(0.5F),
                        value = editTipText,
                        onValueChange = {
                            onTipTextChanged(it)
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSaveTipClicked()
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onCancelEditTipClicked()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        } else {
            TextWithAppendedContent(
                text = "${tip.tip.tipNumber}.  ${tip.tip.text}",
                onTextClicked = { onTipFocusChanged(tip) },
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
                                    onEditTipClicked(tip)
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
                                    onDeleteTipClicked(tip)
                                }
                        )
                    }
                }
            )
            if(showConfirmDeleteTipDialog) {
                AlertDialog(
                    onDismissRequest = { onCancelConfirmDelete() },
                    title = { Text(text = "Confirm Delete Tip") },
                    text = { Text(text = "Are you sure you want to delete this tip?") },
                    confirmButton = {
                        TextButton(
                            onClick = { onConfirmDeleteTipClicked() }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { onCancelConfirmDelete() }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}