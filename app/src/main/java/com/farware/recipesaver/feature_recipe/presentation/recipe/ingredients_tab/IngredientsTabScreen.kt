package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldWithError
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientsChip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab.StepsTabEvent
import com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab.TipsTabEvent
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.Spacing
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.md_theme_dark_onPrimaryContainer
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@Composable
fun IngredientsTabScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: IngredientsTabViewModel = hiltViewModel()
) {
    if(viewModel.state.value.showSnackbar) {
        viewModel.state.value.copy(
            showSnackbar = false
        )
        LaunchedEffect(key1 = "") {
            snackbarHostState.showSnackbar(viewModel.state.value.message)
        }
    }

    IngredientsTabContent(
        ingredients = viewModel.state.value.ingredientFocus,
        newAmountText = viewModel.state.value.newAmountText,
        newMeasureText = viewModel.state.value.newMeasureText,
        newIngredientText = viewModel.state.value.newIngredientText,
        editAmountText = viewModel.state.value.editAmountText,
        editMeasureText = viewModel.state.value.editMeasureText,
        editIngredientText = viewModel.state.value.editIngredientText,
        showEditIngredientDialog= viewModel.state.value.showEditIngredientDialog,
        showConfirmDeleteIngredientDialog = viewModel.state.value.showDeleteIngredientDialog,
        showNewIngredientDialog = viewModel.state.value.showNewIngredientDialog,
        onEditAmountTextChanged = { viewModel.onEvent(IngredientsTabEvent.EditAmountTextChanged(it)) },
        onEditMeasureTextChanged = { viewModel.onEvent(IngredientsTabEvent.EditMeasureTextChanged(it)) },
        onEditIngredientTextChanged = { viewModel.onEvent(IngredientsTabEvent.EditIngredientTextChanged(it)) },
        onNewAmountTextChanged = { viewModel.onEvent(IngredientsTabEvent.NewAmountTextChanged(it)) },
        onNewMeasureTextChanged = { viewModel.onEvent(IngredientsTabEvent.NewMeasureTextChanged(it)) },
        onNewIngredientTextChanged = { viewModel.onEvent(IngredientsTabEvent.NewIngredientTextChanged(it)) },
        onSaveNewIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.SaveNewIngredient) },
        onIngredientFocusChanged = { viewModel.onEvent(IngredientsTabEvent.IngredientFocusChanged(it)) },
        onSaveIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.SaveEditIngredient) },
        onCancelEditIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.CancelEditIngredient) },
        onEditIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.EditIngredient(it)) },
        onDeleteIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.DeleteIngredient(it)) },
        onConfirmDeleteIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.ConfirmDeleteIngredient) },
        onCancelConfirmDelete = { viewModel.onEvent(IngredientsTabEvent.CancelConfirmDeleteIngredient) },
        onToggleNewIngredientDialog = { viewModel.onEvent(IngredientsTabEvent.ToggleNewIngredientDialog) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsTabContent(
    ingredients: List<IngredientFocus>,
    newAmountText: String,
    newMeasureText: String,
    newIngredientText: String,
    editAmountText: String,
    editMeasureText: String,
    editIngredientText: String,
    showEditIngredientDialog: Boolean,
    showConfirmDeleteIngredientDialog: Boolean,
    showNewIngredientDialog: Boolean,
    onEditAmountTextChanged: (String) -> Unit,
    onEditMeasureTextChanged: (String) -> Unit,
    onEditIngredientTextChanged: (String) -> Unit,
    onNewAmountTextChanged: (String) -> Unit,
    onNewMeasureTextChanged: (String) -> Unit,
    onNewIngredientTextChanged: (String) -> Unit,
    onSaveNewIngredientClicked: () -> Unit,
    onIngredientFocusChanged: (IngredientFocus) -> Unit,
    onSaveIngredientClicked: () -> Unit,
    onCancelEditIngredientClicked: () -> Unit,
    onEditIngredientClicked: (IngredientFocus) -> Unit,
    onDeleteIngredientClicked: (IngredientFocus) -> Unit,
    onConfirmDeleteIngredientClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit,
    onToggleNewIngredientDialog: () -> Unit
) {
    if(showNewIngredientDialog) {
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                onToggleNewIngredientDialog()
            },
            title = {
                Text(text = "New Ingredient")
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextFieldWithError(
                        text = newAmountText,
                        onTextChanged = { onNewAmountTextChanged(it) },
                        label = "Amount",
                        onFocusChanged = {
                            // TODO: Add if necessary do the amount and measure parse
                        }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                    OutlinedTextFieldWithError(
                        text = newMeasureText,
                        onTextChanged = { onNewMeasureTextChanged(it) },
                        label = "Measure",
                        onFocusChanged = {
                            // TODO: Add if necessary do the amount and measure parse
                            // onNewAmountAndMeasureFocusChanged
                        }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                    OutlinedTextFieldWithError(
                        text = newIngredientText,
                        onTextChanged = { onNewIngredientTextChanged(it) },
                        label = "Ingredient",
                        onFocusChanged = {
                            // TODO: Add if necessary do the ingredient parse
                            // onNewIngredientFocusChanged

                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveNewIngredientClicked()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onToggleNewIngredientDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //TODO: Colors
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                items(ingredients) { ingredient ->
                    IngredientsChip(
                        ingredient = ingredient,
                        editAmountText = editAmountText,
                        editMeasureText = editMeasureText,
                        editIngredientText = editIngredientText,
                        isFocused = ingredient.focused,
                        showEditIngredientDialog = showEditIngredientDialog,
                        showConfirmDeleteIngredientDialog = showConfirmDeleteIngredientDialog,
                        onEditAmountTextChanged = { onEditAmountTextChanged(it) },
                        onEditMeasureTextChanged = { onEditMeasureTextChanged(it) },
                        onEditIngredientTextChanged = { onEditIngredientTextChanged(it) },
                        onIngredientFocusChanged = { onIngredientFocusChanged(it) },
                        onSaveIngredientClicked = { onSaveIngredientClicked() },
                        onCancelEditIngredientClicked = { onCancelEditIngredientClicked() },
                        onEditIngredientClicked = { onEditIngredientClicked(it) },
                        onDeleteIngredientClicked = { onDeleteIngredientClicked(it) },
                        onConfirmDeleteIngredientClicked = { onConfirmDeleteIngredientClicked() },
                        onCancelConfirmDelete = { onCancelConfirmDelete() }
                    )
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { onToggleNewIngredientDialog() },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new ingredient") },
                text = { Text(text = "New Ingredient") },
            )
        }
    }
}