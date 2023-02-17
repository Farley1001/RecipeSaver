package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.AssistChipDefaults.assistChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldWithError
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.chipShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsChip(
    ingredient: IngredientFocus,
    editAmountText: String,
    editMeasureText: String,
    editIngredientText: String,
    isFocused: Boolean,
    showEditIngredientDialog: Boolean,
    showConfirmDeleteIngredientDialog: Boolean,
    onEditAmountTextChanged: (String) -> Unit,
    onEditMeasureTextChanged: (String) -> Unit,
    onEditIngredientTextChanged: (String) -> Unit,
    onIngredientFocusChanged: (IngredientFocus) -> Unit,
    onSaveIngredientClicked: () -> Unit,
    onCancelEditIngredientClicked: () -> Unit,
    onEditIngredientClicked: (IngredientFocus) -> Unit,
    onDeleteIngredientClicked: (IngredientFocus) -> Unit,
    onConfirmDeleteIngredientClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit
) {
    Column(
        Modifier
            //.fillMaxSize()
            .wrapContentSize()
    ) {
        if (showEditIngredientDialog) {
            AlertDialog(
                modifier = Modifier
                    .customDialogPosition(CustomDialogPosition.TOP)
                    .padding(top = 20.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    onCancelEditIngredientClicked()
                },
                title = {
                    Text(text = "Edit Ingredient from chip")
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        OutlinedTextFieldWithError(
                            text = editAmountText,
                            onTextChanged = { onEditAmountTextChanged(it)},
                            label = "Amount",
                            onFocusChanged = {
                                // TODO: Add focus change if needed
                            }
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                        OutlinedTextFieldWithError(
                            text = editMeasureText,
                            onTextChanged = { onEditMeasureTextChanged(it)},
                            label = "Measure",
                            onFocusChanged = {
                                // TODO: Add focus change if needed
                            }
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                        OutlinedTextFieldWithError(
                            text = editIngredientText,
                            onTextChanged = { onEditIngredientTextChanged(it) },
                            label = "Ingredient",
                            onFocusChanged = {
                                // TODO: Add focus change if needed
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSaveIngredientClicked()
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onCancelEditIngredientClicked()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        } else {
            ElevatedAssistChip(
                onClick = { onIngredientFocusChanged(ingredient) },
                label = {
                    Text(
                        text = "${ingredient.fullIngredient.amount}.${ingredient.fullIngredient.measure} ${ingredient.fullIngredient.ingredient}"
                    )
                },
                shape = chipShape,
                colors = assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                        alpha = .5f
                    )
                ),
                trailingIcon = {
                    if (isFocused) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                            Modifier
                                .size(AssistChipDefaults.IconSize)
                                .clickable {
                                    onEditIngredientClicked(ingredient)
                                }
                        )
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Localized description",
                            Modifier
                                .size(AssistChipDefaults.IconSize)
                                .clickable {
                                    onDeleteIngredientClicked(ingredient)
                                }
                        )
                    }
                }
            )
        }
        if (showConfirmDeleteIngredientDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    onCancelConfirmDelete()
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
                            onConfirmDeleteIngredientClicked()
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