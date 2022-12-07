package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.farware.recipesaver.feature_recipe.presentation.recipe.ingredients_tab.IngredientsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.steps_tab.StepsTabScreen
import com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab.TipsTabScreen

sealed class TabItem(val title: String, val fabText: String, val screen: @Composable () -> Unit?) {
    /*object Steps : TabItem( title = "Recipe", {
        StepsTabScreen(
            //TODO: Check Color
            categoryColor = MaterialTheme.colorScheme.primary,
            onCategoryColor = MaterialTheme.colorScheme.onPrimary
        )
    })*/
    object Steps : TabItem( title = "Steps", fabText = "Add Step", screen = { } )
    object Ingredients : TabItem(title = "Ingredients", fabText = "Add Ingredient", { })
    object Tips : TabItem( title = "Tips", fabText = "Add Tip", { })
}
