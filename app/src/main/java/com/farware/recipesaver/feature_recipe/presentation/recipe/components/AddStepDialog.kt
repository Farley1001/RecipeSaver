package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStepDialog(
    onSaveClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    val text = remember { mutableStateOf("") }
    val isFocused = remember { mutableStateOf(false) }
    val isEditMode = remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(true) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }

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
            Text(text = "Edit Step")
        },
        text = {
            TextField(
                modifier = Modifier.fillMaxHeight(0.5F),
                value = text.value,
                onValueChange = { text.value = it })
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openEditDialog.value = false
                    isEditMode.value = false
                    // TODO: Save the new step
                    onSaveClicked(-1)
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
}