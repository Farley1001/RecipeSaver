package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.window.PopupProperties
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.outlinedTextFieldShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithDropdown(
    modifier: Modifier = Modifier,
    dropdownList: List<MatchTo>,
    label: String = "",
    text: String,
    dropDownExpanded: Boolean,
    textChanged: (String) -> Unit,
    setTextFromDropdown: (String) -> Unit,
    onDismissRequest: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Box(modifier) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused)
                        onDismissRequest()
                },
            value = text,
            onValueChange = { textChanged(it)},
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(),
            shape = outlinedTextFieldShape,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        DropdownMenu(
            expanded = dropDownExpanded,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = onDismissRequest
        ) {
            dropdownList.forEach { matchTo ->
                DropdownMenuItem(
                    onClick = {
                        setTextFromDropdown(matchTo.text)
                    },
                    text = { Text(text = matchTo.text) }
                )
            }
        }
    }
}