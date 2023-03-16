package com.farware.recipesaver.feature_recipe.presentation.recipe.tips_tab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val focusManager = LocalFocusManager.current



    TipsTabContent(
        focusManager = focusManager,
        tips = viewModel.state.value.tipFocus,
        newTipText = viewModel.state.value.newTipText,
        editTipText = viewModel.state.value.editTipText,
        showEditTipDialog = viewModel.state.value.showEditTipDialog,
        showConfirmDeleteTipDialog = viewModel.state.value.showDeleteTipDialog,
        showNewTipDialog = viewModel.state.value.showNewTipDialog,
        onEditTipTextChanged = { viewModel.onEvent(TipsTabEvent.EditTipTextChanged(it)) },
        onNewTipTextChanged = { viewModel.onEvent(TipsTabEvent.NewTipTextChanged(it)) },
        onSaveNewTipClicked = { viewModel.onEvent(TipsTabEvent.SaveNewTip) },
        onTipFocusChanged = { viewModel.onEvent(TipsTabEvent.TipFocusChanged(it)) },
        onSaveTipClicked = { viewModel.onEvent(TipsTabEvent.SaveEditTip) },
        onCancelEditTipClicked = { viewModel.onEvent(TipsTabEvent.CancelEditTip) },
        onEditTipClicked = { viewModel.onEvent(TipsTabEvent.EditTip(it))},
        onDeleteTipClicked = { viewModel.onEvent(TipsTabEvent.DeleteTip(it)) },
        onConfirmDeleteTipClicked = { viewModel.onEvent(TipsTabEvent.ConfirmDeleteTip) },
        onCancelConfirmDelete = { viewModel.onEvent(TipsTabEvent.CancelConfirmDeleteTip) },
        onToggleNewTipDialog = { viewModel.onEvent(TipsTabEvent.ToggleNewTipDialog) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsTabContent(
    focusManager: FocusManager,
    tips: List<TipFocus?>,
    newTipText: String,
    editTipText: String,
    showEditTipDialog: Boolean,
    showConfirmDeleteTipDialog: Boolean,
    showNewTipDialog: Boolean,
    onEditTipTextChanged: (String) -> Unit,
    onNewTipTextChanged: (String) -> Unit,
    onSaveNewTipClicked: () -> Unit,
    onTipFocusChanged: (TipFocus) -> Unit,
    onSaveTipClicked: () -> Unit,
    onCancelEditTipClicked: () -> Unit,
    onEditTipClicked: (TipFocus) -> Unit,
    onDeleteTipClicked: (TipFocus) -> Unit,
    onConfirmDeleteTipClicked: () -> Unit,
    onCancelConfirmDelete: () -> Unit,
    onToggleNewTipDialog: () -> Unit
) {
    if(showNewTipDialog) {
        AlertDialog(
            modifier = Modifier
                .customDialogPosition(CustomDialogPosition.TOP)
                .padding(top = 20.dp),
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                onToggleNewTipDialog()
            },
            title = {
                Text(text = "New Tip")
            },
            text = {
                TextField(
                    modifier = Modifier.fillMaxHeight(0.5F),
                    value = newTipText,
                    onValueChange = {
                        onNewTipTextChanged(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                    })
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveNewTipClicked()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onToggleNewTipDialog()
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
                            editTipText = editTipText,
                            isFocused = tip.focused,
                            showEditTipDialog = showEditTipDialog,
                            showConfirmDeleteTipDialog = showConfirmDeleteTipDialog,
                            onTipTextChanged = { onEditTipTextChanged(it) },
                            onTipFocusChanged = { onTipFocusChanged(it) },
                            onSaveTipClicked = { onSaveTipClicked() },
                            onCancelEditTipClicked = { onCancelEditTipClicked() },
                            onEditTipClicked = { onEditTipClicked(it) },
                            onDeleteTipClicked = { onDeleteTipClicked(it) },
                            onConfirmDeleteTipClicked = { onConfirmDeleteTipClicked() },
                            onCancelConfirmDelete = { onCancelConfirmDelete() }
                        )
                    }
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 15.dp, end = 15.dp),
                onClick = { onToggleNewTipDialog() },
                shape = fabShape,
                icon = { Icon(Icons.Filled.Add, "Add new tip") },
                text = { Text(text = "New Tip") },
            )
        }
    }
}