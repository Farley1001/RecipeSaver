package com.farware.recipesaver.feature_recipe.presentation.recipes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.domain.util.RecipeOrder
import com.farware.recipesaver.feature_recipe.presentation.components.DefaultRadioButton
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun OrderSection(
    recipeOrder: RecipeOrder = RecipeOrder.Date(OrderType.Descending),
    onOrderChange: (RecipeOrder) -> Unit
) {
    Surface(

    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = "Recipe",
                    selected = recipeOrder is RecipeOrder.Name,
                    onSelect = { onOrderChange(RecipeOrder.Name(recipeOrder.orderType)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                DefaultRadioButton(
                    text = "Category",
                    selected = recipeOrder is RecipeOrder.Category,
                    onSelect = { onOrderChange(RecipeOrder.Category(recipeOrder.orderType)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                DefaultRadioButton(
                    text = "Date",
                    selected = recipeOrder is RecipeOrder.Date,
                    onSelect = { onOrderChange(RecipeOrder.Date(recipeOrder.orderType)) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = "Ascending",
                    selected = recipeOrder.orderType is OrderType.Ascending,
                    onSelect = { onOrderChange(recipeOrder.copy(OrderType.Ascending)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                DefaultRadioButton(
                    text = "Descending",
                    selected = recipeOrder.orderType is OrderType.Descending,
                    onSelect = { onOrderChange(recipeOrder.copy(OrderType.Descending)) }
                )
            }
        }
    }
}