package com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab

import com.farware.recipesaver.feature_recipe.presentation.recipe.components.IngredientFocus

sealed class IngredientsTabEvent{
    data class IngredientFocusChanged(val ingredientFocus: IngredientFocus) : IngredientsTabEvent()
    data class DeleteIngredient(val ingredient: IngredientFocus): IngredientsTabEvent()
    object CancelConfirmDeleteIngredient: IngredientsTabEvent()
    object ConfirmDeleteIngredient: IngredientsTabEvent()
    data class EditIngredient(val ingredient: IngredientFocus): IngredientsTabEvent()
    data class EditAmountTextChanged(val amountText: String): IngredientsTabEvent()
    data class EditMeasureTextChanged(val measureText: String): IngredientsTabEvent()
    data class EditIngredientTextChanged(val ingredientText: String): IngredientsTabEvent()
    object SaveEditIngredient: IngredientsTabEvent()
    object CancelEditIngredient: IngredientsTabEvent()
    object ToggleNewIngredientDialog: IngredientsTabEvent()
    data class NewAmountTextChanged(val amountText: String): IngredientsTabEvent()
    data class NewMeasureTextChanged(val measureText: String): IngredientsTabEvent()
    data class NewIngredientTextChanged(val ingredientText: String): IngredientsTabEvent()
    object SaveNewIngredient: IngredientsTabEvent()

}
