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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientsChip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.StepFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab.StepsTabEvent
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@Composable
fun IngredientsTabScreen(
    viewModel: IngredientsTabViewModel = hiltViewModel()
) {
    val text = remember { mutableStateOf("") }
    val showNewIngredientDialog = remember { mutableStateOf(false) }

    IngredientsTabContent(
        ingredients = viewModel.state.value.ingredientFocus,
        newIngredient = viewModel.newIngredient.value,
        text = text,
        showNewIngredientDialog = showNewIngredientDialog,
        ingredientFocusChanged = { viewModel.onEvent(IngredientsTabEvent.IngredientFocusChanged(it)) },
        saveIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.SaveIngredient(it)) },
        deleteIngredientClicked = { viewModel.onEvent(IngredientsTabEvent.DeleteIngredient(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsTabContent(
    ingredients: List<IngredientFocus>,
    newIngredient: FullRecipeIngredient,
    text: MutableState<String>,
    showNewIngredientDialog: MutableState<Boolean>,
    ingredientFocusChanged: (IngredientFocus) -> Unit,
    saveIngredientClicked: (FullRecipeIngredient) -> Unit,
    deleteIngredientClicked: (FullRecipeIngredient) -> Unit
) {
    //val showNewIngredientDialog = remember { mutableStateOf(false) }
    //val text = remember { mutableStateOf("") }

    if(showNewIngredientDialog.value) {
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
                Text(text = "Edit Ingredient")
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
                        showNewIngredientDialog.value = false
                        saveIngredientClicked(
                            newIngredient.copy(
                                //recipeIngredientWithIngredient.recipeIngredient
                            )
                        )
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
                        onChangeFocus = { ingredientFocusChanged(it) },
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