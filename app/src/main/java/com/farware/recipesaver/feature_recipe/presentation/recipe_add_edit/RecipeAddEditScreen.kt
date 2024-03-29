package com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.components.DropdownWithLabel
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.UiEvent
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeAddEditScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: RecipeAddEditViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Success -> {
                    snackbarHostState.showSnackbar(
                        message = "Save Succeeded"
                    )
                }
            }
        }
    }

    RecipeAddEditContent(
        snackbarHostState = snackbarHostState,
        categories = viewModel.state.value.categories,
        recipeName = viewModel.recipe.value!!.name,
        recipeDescription = viewModel.recipe.value!!.description,
        selectedCategoryIndex = viewModel.state.value.selectedCategoryIndex,
        onRecipeNameTextChange = { viewModel.onEvent(RecipeAddEditEvent.RecipeNameTextChange(it)) },
        onRecipeDescriptionTextChange = { viewModel.onEvent(RecipeAddEditEvent.RecipeDescriptionTextChange(it)) },
        onCategorySelected = { viewModel.onEvent(RecipeAddEditEvent.NewCategorySelected(it)) },
        onSaveRecipeClick = { viewModel.onEvent(RecipeAddEditEvent.SaveRecipe) },
        onCancelRecipeClick = { viewModel.onEvent(RecipeAddEditEvent.CancelRecipe) }
    )
}

@Composable
fun RecipeAddEditContent(
    snackbarHostState: SnackbarHostState,
    categories: List<Category>,
    recipeName: String,
    recipeDescription: String,
    selectedCategoryIndex: Int,
    onRecipeNameTextChange: (name: String) -> Unit,
    onRecipeDescriptionTextChange: (name: String) -> Unit,
    onCategorySelected: (Long) -> Unit,
    onSaveRecipeClick: () -> Unit,
    onCancelRecipeClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(padding),
            onDismissRequest = { onCancelRecipeClick() },
            title = {
                Text(text = "Add Recipe")
            },
            text = {
                val focusManager = LocalFocusManager.current
                Column() {//modifier = Modifier.fillMaxSize()
                    Row(
                        modifier = Modifier
                            //.padding(start = 50.dp)
                            .fillMaxWidth(),
                        //horizontalArrangement = Arrangement.Center
                    ) {
                        if (categories.isNotEmpty() && selectedCategoryIndex != -1) {
                            DropdownWithLabel(
                                labelText = "Category",
                                initialIndex = selectedCategoryIndex,
                                items = categories,
                                onDropdownItemSelected = { onCategorySelected(it) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    OutlinedTextField(
                        value = recipeName,
                        label = { Text(text = "Recipe Name") },
                        onValueChange = { onRecipeNameTextChange(it) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        })
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    OutlinedTextField(
                        value = recipeDescription,
                        label = { Text(text = "Recipe Description") },
                        onValueChange = { onRecipeDescriptionTextChange(it) },
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
                        onSaveRecipeClick()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onCancelRecipeClick()  }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
