package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus

data class IngredientsTabState(
    val recipeIngredients: List<FullRecipeIngredient?> = emptyList(),
    val ingredientFocus: List<IngredientFocus> = emptyList(),
    val measures: List<Measure> = emptyList(),
    val allIngredients: List<Ingredient> = emptyList(),
    val editedIngredient: FullRecipeIngredient = FullRecipeIngredient.new(),
    val ingredientToDelete: FullRecipeIngredient = FullRecipeIngredient.new(),
    val editAmountText: String = "",
    val editMeasureText: String = "",
    val editIngredientText: String = "",
    val newAmountText: String = "",
    val newMeasureText: String = "",
    val newIngredientText: String = "",
    val showEditIngredientDialog: Boolean = false,
    val showDeleteIngredientDialog: Boolean = false,
    val showNewIngredientDialog: Boolean = false,
    val showSnackbar: Boolean = false,
    val message: String = ""
)
