package com.farware.recipesaver.feature_recipe.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            enabled = enabled,
            onClick = onSelect,
            // TODO:  fix or remove
//            colors = RadioButtonDefaults.colors(
//                selectedColor = WhiteSmoke,
//                unselectedColor = Color.Gray,
//                disabledColor = Color.Gray.copy(alpha = 0.3f)
//            )
        )
        Text(text = text,
            // TODO:  check size
            style = MaterialTheme.typography.bodyMedium
        )
    }
}