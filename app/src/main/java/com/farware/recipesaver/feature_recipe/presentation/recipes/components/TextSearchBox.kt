package com.farware.recipesaver.feature_recipe.presentation.recipes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSearchBox(
    labelText: String,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    onClearSearchText: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = TextStyle(
                //TODO: Check Color and size
                //color = MaterialTheme.extraColors.stfTextColor,
                fontSize = 24.sp
            ),
            label = {
                if (!isHintVisible) {
                    Text(
                        text = labelText,
                        //TODO: Check Color
                        //color = MaterialTheme.extraColors.stfLabelColor
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { onClearSearchText() }) {
                    Icon(
                        Icons.Filled.Clear,
                        "Clear Icon"
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                // TODO: Colors removed
                //cursorColor = MaterialTheme.extraColors.stfCursorColor,
                //backgroundColor = MaterialTheme.extraColors.stfBgColor,
                //focusedIndicatorColor = MaterialTheme.extraColors.stfFocusIndicatorColor
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if (isHintVisible) {
            Text(text = hint,
                style = textStyle,
                //TODO: Check Color
                //color = MaterialTheme.extraColors.stfTextColor,
                modifier = Modifier.padding(start = 18.dp, top = 20.dp)
            )
        }
    }
}