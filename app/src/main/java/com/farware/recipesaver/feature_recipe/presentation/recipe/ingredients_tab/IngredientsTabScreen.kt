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
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.Spacing
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
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
    val showNewIngredientDialog = remember { mutableStateOf(false) }

    if(viewModel.state.value.showSnackbar) {
        LaunchedEffect(key1 = "") {
            snackbarHostState.showSnackbar(viewModel.state.value.message)
        }
        viewModel.state.value.copy(
            showSnackbar = false
        )
    }


    IngredientsTabContent(
        ingredients = viewModel.state.value.ingredientFocus,
        onIngredientFocusChanged = { viewModel.onEvent(IngredientsTabEvent.IngredientFocusChanged(it)) },
        newFullIngredient = viewModel.newFullIngredient.value,
        dialogAmountAndMeasure = "${viewModel.recipeIngredient.value.amount} ${viewModel.recipeIngredient.value.measure}" ,
        onDialogAmountAndMeasureChanged = { },
        onDialogAmountAndMeasureFocusChanged = {},
        dialogIngredient = "${viewModel.recipeIngredient.value.ingredient}",
        onDialogIngredientChanged = {  },
        onDialogIngredientFocusChanged = {  },
        showNewIngredientDialog = showNewIngredientDialog,
        saveIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.SaveIngredient(it)) },
        deleteIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.DeleteIngredient(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsTabContent(
    ingredients: List<IngredientFocus>,
    onIngredientFocusChanged: (IngredientFocus) -> Unit,
    newFullIngredient: (IngredientFocus),
    dialogAmountAndMeasure: String,
    onDialogAmountAndMeasureChanged: (String) -> Unit,
    onDialogAmountAndMeasureFocusChanged: (FocusState) -> Unit,
    dialogIngredient: String,
    onDialogIngredientChanged: (String) -> Unit,
    onDialogIngredientFocusChanged: (FocusState) -> Unit,
    showNewIngredientDialog: MutableState<Boolean>,
    saveIngredientClicked: (IngredientFocus) -> Unit,
    deleteIngredientClicked: (FullRecipeIngredient) -> Unit
) {
    if(showNewIngredientDialog.value) {
        val newDialogAmountAndMeasure = remember { mutableStateOf(dialogAmountAndMeasure) }
        val newDialogIngredient = remember { mutableStateOf(dialogIngredient) }

        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showNewIngredientDialog.value = false
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
                        text = newDialogAmountAndMeasure.value,
                        onTextChanged = { newDialogAmountAndMeasure.value = it },
                        label = "Amount and Measure",
                        onFocusChanged = { onDialogAmountAndMeasureFocusChanged(it) }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                    OutlinedTextFieldWithError(
                        text = newDialogIngredient.value,
                        onTextChanged = { newDialogIngredient.value = it },
                        label = "Ingredient",
                        onFocusChanged = { onDialogIngredientFocusChanged(it) }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNewIngredientDialog.value = false
                        // TODO: Save new ingredient
                        newFullIngredient.amountAndMeasure = newDialogAmountAndMeasure.value
                        newFullIngredient.ingredient = newDialogIngredient.value
                        saveIngredientClicked(newFullIngredient)
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNewIngredientDialog.value = false
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
                        focused = ingredient.focused,
                        onChangeFocus = { onIngredientFocusChanged(it) },
                        onSaveClicked = { saveIngredientClicked(it) },
                        onDeleteClicked = { deleteIngredientClicked(it) },
                    )
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { showNewIngredientDialog.value = true },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new ingredient") },
                text = { Text(text = "New Ingredient") },
            )
        }
    }
}