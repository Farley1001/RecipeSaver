package com.farware.recipesaver.feature_recipe.presentation.recipes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.domain.util.RecipeSearch
import com.farware.recipesaver.feature_recipe.presentation.components.DefaultRadioButton
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun SearchSection(
    searchStringText: String,
    searchStringHint: String,
    isHintVisible: Boolean,
    searchFieldsEnabled: Boolean,
    selectedSearch: RecipeSearch,
    onSearchChange: (RecipeSearch) -> Unit,
    onSearchBoxValueChange: (String) -> Unit,
    onSearchBoxFocusChange: (FocusState) -> Unit,
    onSearchBoxClearClick: () -> Unit,
) {
    Surface(
        // TODO: Check param ELEVATION
        //elevation = 1.dp,
        // TODO: Check Color changed from primary
        //color = MaterialTheme.colorScheme.primary,
        //contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextSearchBox(
                    labelText = "Text Search",
                    text = searchStringText,
                    hint = searchStringHint,
                    isHintVisible = isHintVisible,
                    singleLine = true,
                    // TODO: Check font size
                    //textStyle = typography.h5,
                    onValueChange = {
                        onSearchBoxValueChange(it)
                    },
                    onFocusChange = {
                        onSearchBoxFocusChange(it)
                    },
                    onClearSearchText = {
                        onSearchBoxClearClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Row() {
                DefaultRadioButton(
                    text = "Category",
                    enabled = searchFieldsEnabled,
                    selected = selectedSearch is RecipeSearch.Category,
                    onSelect = { onSearchChange(RecipeSearch.Category()) }
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))
                DefaultRadioButton(
                    text = "Name",
                    enabled = searchFieldsEnabled,
                    selected = selectedSearch is RecipeSearch.Name,
                    onSelect = { onSearchChange(RecipeSearch.Name()) }
                )

            }
            Row() {
                DefaultRadioButton(
                    text = "Ingredients",
                    enabled = searchFieldsEnabled,
                    selected = selectedSearch is RecipeSearch.Ingredients,
                    onSelect = { onSearchChange(RecipeSearch.Ingredients()) }
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))
                DefaultRadioButton(
                    text = "Directions",
                    enabled = searchFieldsEnabled,
                    selected = selectedSearch is RecipeSearch.Directions,
                    onSelect = { onSearchChange(RecipeSearch.Directions()) }
                )
            }
        }
    }
}