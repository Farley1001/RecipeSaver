package com.farware.recipesaver.feature_recipe.presentation.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.raedapps.alwan.rememberAlwanState
import com.raedapps.alwan.ui.Alwan

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerDialog(
    dialogTitle: String,
    backgroundColor: Color,
    currentColor: Color,
    currentOnColor: Color,
    onChangeColorClick: (Int) -> Unit,
    onDismissDialogClick: () -> Unit
) {
    val selectedColor = remember{ mutableStateOf(currentColor) }
    val hexString = remember { mutableStateOf(java.lang.String.format("#%06X", 0xFFFFFF and selectedColor.value.toArgb())) }
    AlertDialog(
        //modifier = Modifier
        //.height(800.dp)
        //.padding(top = 20.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
            onDismissDialogClick()
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "")
                    Alwan(
                        state = rememberAlwanState(initialColor = selectedColor.value),
                        modifier = Modifier
                            .background(backgroundColor)
                            .width(200.dp),
                        onColorChanged = {
                            selectedColor.value = it
                            hexString.value = String.format(
                                "#%06X",
                                0xFFFFFF and selectedColor.value.toArgb()
                            )
                        },
                    )
                }
                //Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.padding(bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = hexString.value,
                        color = selectedColor.value
                    )
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .background(selectedColor.value)
                    ) {
                        Text(
                            text = dialogTitle,
                            color = currentOnColor
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeColorClick(selectedColor.value.toArgb())
                }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissDialogClick()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}