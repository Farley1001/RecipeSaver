package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.AssistChipDefaults.assistChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.chipShape
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsChip(
    ingredient: IngredientFocus,
    focused: Boolean,
    onChangeFocus: (IngredientFocus) -> Unit,
    onSaveClicked: (FullRecipeIngredient) -> Unit,
    onDeleteClicked: (FullRecipeIngredient) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    val displayText = remember { mutableStateOf("") }
    val isFocused = remember { mutableStateOf(false) }
    val isEditMode = remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(true) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }
    text.value = ingredient.fullIngredient.ingredient
    displayText.value = "${ingredient.fullIngredient.amount} ${ingredient.fullIngredient.measure} ${ingredient.fullIngredient.ingredient}"

    if(!focused) {
        isFocused.value = false
        isEditMode.value = false
    } else {
        isFocused.value = true
    }

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
                Text(text = "Edit Ingredient")
            },
            text = {
                TextField(
                    //modifier = Modifier.fillMaxHeight(0.5F),
                    value = displayText.value,
                    onValueChange = {
                        displayText.value = it
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openEditDialog.value = false
                        isEditMode.value = false
                        // TODO: Save the ingredient
                        onSaveClicked(
                            // TODO: how to change
                            // 2field for measure and a field for ingredient name
                            //step.copy(
                            //    step = step.step.copy(
                            //        text = text.value
                            //    )
                            //)
                            ingredient.fullIngredient
                        )
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
        ElevatedAssistChip(
            onClick = { onChangeFocus(ingredient) },
            label = { Text(text = displayText.value) },
            shape = chipShape,
            colors = assistChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = .5f
                )
            ),
            trailingIcon = {
                if (isFocused.value) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Localized description",
                        Modifier
                            .size(AssistChipDefaults.IconSize)
                            .clickable {
                                isEditMode.value = true
                            }
                    )
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Localized description",
                        Modifier
                            .size(AssistChipDefaults.IconSize)
                            .clickable {
                                isFocused.value = false
                                openConfirmDeleteDialog.value = true
                            }
                    )
                }
            }
        )
    }
    if(openConfirmDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openConfirmDeleteDialog.value = false
            },
            title = {
                Text(text = "Confirm Delete Ingredient")
            },
            text = {
                Text(text = "Are you sure you want to delete this ingredient?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openConfirmDeleteDialog.value = false
                        onDeleteClicked(ingredient.fullIngredient)
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