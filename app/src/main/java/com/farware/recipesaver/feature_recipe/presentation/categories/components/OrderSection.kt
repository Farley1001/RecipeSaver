package com.farware.recipesaver.feature_recipe.presentation.categories.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.domain.util.CategoryOrder
import com.farware.recipesaver.feature_recipe.domain.util.OrderType
import com.farware.recipesaver.feature_recipe.presentation.components.DefaultRadioButton
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun OrderSection(
    categoryOrder: CategoryOrder = CategoryOrder.Name(OrderType.Ascending),
    onOrderChange: (CategoryOrder) -> Unit
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
                    text = "Category",
                    selected = categoryOrder is CategoryOrder.Name,
                    onSelect = { onOrderChange(CategoryOrder.Name(categoryOrder.orderType)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                DefaultRadioButton(
                    text = "Category Color",
                    selected = categoryOrder is CategoryOrder.Color,
                    onSelect = { onOrderChange(CategoryOrder.Color(categoryOrder.orderType)) }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumSmall))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = "Ascending",
                    selected = categoryOrder.orderType is OrderType.Ascending,
                    onSelect = { onOrderChange(categoryOrder.copy(OrderType.Ascending)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                DefaultRadioButton(
                    text = "Descending",
                    selected = categoryOrder.orderType is OrderType.Descending,
                    onSelect = { onOrderChange(categoryOrder.copy(OrderType.Descending)) }
                )
            }
        }
    }
}