package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.components.DropdownWithLabel

@Composable
fun ChangeCategoryDialog(
    categories: List<Category>,
    selectedIndex: Int,
    onSaveCategoryClick: () -> Unit,
    onCancelCategoryClick: () -> Unit,
    newCategorySelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelCategoryClick() },
        title = {
            Text(text = "Select a new Category")
        },
        text = {
            DropdownWithLabel(
                labelText = "",
                initialIndex = selectedIndex,
                items = categories,
                onDropdownItemSelected = {  newCategorySelected(it) }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSaveCategoryClick()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onCancelCategoryClick()  }
            ) {
                Text("Cancel")
            }
        }
    )
}