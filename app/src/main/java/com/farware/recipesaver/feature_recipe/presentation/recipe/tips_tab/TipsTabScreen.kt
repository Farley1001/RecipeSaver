package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipFocus
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TipTextWithIcon
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.fabShape
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing
import com.farware.recipesaver.feature_recipe.presentation.util.CustomDialogPosition
import com.farware.recipesaver.feature_recipe.presentation.util.customDialogPosition

@Composable
fun TipsTabScreen(
    viewModel: TipsTabViewModel = hiltViewModel(),
) {

    TipsTabContent(
        tips = viewModel.state.value.tipsFocus,
        newTip = viewModel.newTip.value,
        tipFocusChanged = { viewModel.onEvent(TipsTabEvent.TipFocusChanged(it)) },
        saveTipClicked = { viewModel.onEvent(TipsTabEvent.SaveTip(it)) },
        deleteTipClicked = { viewModel.onEvent(TipsTabEvent.DeleteTip(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsTabContent(
    tips: List<TipFocus?>,
    newTip: Tip,
    tipFocusChanged: (TipFocus) -> Unit,
    saveTipClicked: (Tip) -> Unit,
    deleteTipClicked: (Tip) -> Unit
) {
    val showNewTipDialog = remember { mutableStateOf(false) }
    val text = remember { mutableStateOf("") }

    if(showNewTipDialog.value) {
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                showNewTipDialog.value = false
            },
            title = {
                Text(text = "Edit Tip")
            },
            text = {
                TextField(
                    modifier = Modifier.fillMaxHeight(0.5F),
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNewTipDialog.value = false
                        saveTipClicked(
                            newTip.copy(
                                text = text.value
                            )
                        )
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNewTipDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "Tips")
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                items(tips) { tip ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        TipTextWithIcon(
                            tip = tip!!,
                            focused = tip.focused,
                            onChangeFocus = { tipFocusChanged(it) },
                            onSaveClicked = { saveTipClicked(it) },
                            onDeleteClicked = { deleteTipClicked(it) },
                        )
                    }
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { showNewTipDialog.value = true },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new tip") },
                text = { Text(text = "New Tip") },
            )
        }
    }
}