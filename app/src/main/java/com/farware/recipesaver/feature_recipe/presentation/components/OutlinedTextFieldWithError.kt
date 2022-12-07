package com.farware.recipesaver.feature_recipe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    text: String,
    onTextChanged: (String) -> Unit,
    label: String,
    onFocusChanged: (FocusState) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMsg: String = ""
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        // TODO: change to CardColor if needed
        //backgroundColor = MaterialTheme.extraColors.atfBgColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 1.dp, bottom = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
            // TODO: Color
                //.background(MaterialTheme.extraColors.atfBgColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    // TODO: Color
                    //.background(MaterialTheme.extraColors.atfBgColor)
            ) {
                OutlinedTextField(
                    value = text,
                    singleLine = true,
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            // TODO: Color
                            //color = MaterialTheme.extraColors.atfLabelColor,
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { onFocusChanged(it) },
                    onValueChange = {
                        onTextChanged(it)
                    },
                    leadingIcon = leadingIcon,
                    trailingIcon =
                    if (text != "") {
                        trailingIcon
                    } else {
                        null
                    },
                    visualTransformation = visualTransformation,
                    // TODO: Color
                    //colors = TextFieldDefaults.textFieldColors(
                    //    textColor = MaterialTheme.extraColors.atfTextColor,
                    //    backgroundColor = MaterialTheme.extraColors.atfBgColor,
                    //    focusedIndicatorColor = MaterialTheme.extraColors.atfFocusIndicatorColor, //hide the indicator
                    //    unfocusedIndicatorColor = MaterialTheme.extraColors.atfUnFocusIndicatorColor
                    //),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions
                )
            }
            if (isError) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp)
                ) {
                    Text(
                        text = errorMsg,
                        // TODO: Color
                        color = MaterialTheme.colorScheme.error,
                        // TODO: Check size
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}