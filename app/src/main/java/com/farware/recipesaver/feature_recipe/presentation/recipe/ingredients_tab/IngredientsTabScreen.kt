package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import android.view.KeyEvent.ACTION_DOWN
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.components.OutlinedTextFieldWithError
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.*
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
    //val focusManager = LocalFocusManager.current

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
        onToggleNewIngredientDialog = { viewModel.onEvent(IngredientsTabEvent.ToggleNewIngredientDialog) },
        measureDropdownList = viewModel.state.value.measureDropdownList,
        ingredientDropdownList = viewModel.state.value.ingredientDropdownList,
        showMeasureDropdown = viewModel.state.value.showMeasureDropdown,
        showIngredientDropdown = viewModel.state.value.showIngredientDropdown,
        setMeasureTextFromDropdown = { viewModel.onEvent(IngredientsTabEvent.SetMeasureTextFromDropdown(it)) },
        setIngredientTextFromDropdown = { viewModel.onEvent(IngredientsTabEvent.SetIngredientTextFromDropdown(it)) },
        dismissAllDropdowns = { viewModel.onEvent(IngredientsTabEvent.DismissAllDropdowns) }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
    onToggleNewIngredientDialog: () -> Unit,
    measureDropdownList: List<MatchTo>,
    ingredientDropdownList: List<MatchTo>,
    showMeasureDropdown: Boolean,
    showIngredientDropdown: Boolean,
    setMeasureTextFromDropdown: (String) -> Unit,
    setIngredientTextFromDropdown: (String) -> Unit,
    dismissAllDropdowns: () -> Unit
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
                val focusManager = LocalFocusManager.current
                val (amountFieldFocus, measureFieldFocus, ingredientFieldFocus) = remember { FocusRequester.createRefs() }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextFieldWithError(
                        textFieldModifier = Modifier
                            .focusRequester(amountFieldFocus)
                            .focusProperties { next = measureFieldFocus },
                        text = newAmountText,
                        onTextChanged = { onNewAmountTextChanged(it) },
                        label = "Amount",
                        onFocusChanged = {
                            // TODO: Add if necessary do the amount and measure parse
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                    OutlinedTextFieldWithDropdown(
                        modifier = Modifier
                            .focusRequester(measureFieldFocus)
                            .focusProperties { next = ingredientFieldFocus },
                        dropdownList = measureDropdownList,
                        label = "Measure",
                        text = newMeasureText,
                        dropDownExpanded = showMeasureDropdown,
                        textChanged = { onNewMeasureTextChanged(it) },
                        setTextFromDropdown = { setMeasureTextFromDropdown(it) },
                        onDismissRequest = { dismissAllDropdowns() },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        })
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
                    OutlinedTextFieldWithDropdown(
                        modifier = Modifier
                            .focusRequester(ingredientFieldFocus),
                        dropdownList = ingredientDropdownList,
                        label = "Ingredient",
                        text = newIngredientText,
                        dropDownExpanded = showIngredientDropdown,
                        textChanged = { onNewIngredientTextChanged(it) },
                        setTextFromDropdown = { setIngredientTextFromDropdown(it) },
                        onDismissRequest = { dismissAllDropdowns() },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.clearFocus()
                        })
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
                        onCancelConfirmDelete = { onCancelConfirmDelete() },
                        measureDropdownList = measureDropdownList,
                        ingredientDropdownList = ingredientDropdownList,
                        showMeasureDropdown = showMeasureDropdown,
                        showIngredientDropdown = showIngredientDropdown,
                        setMeasureTextFromDropdown = { setMeasureTextFromDropdown(it) },
                        setIngredientTextFromDropdown = { setIngredientTextFromDropdown(it) },
                        dismissAllDropdowns = { dismissAllDropdowns() }
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