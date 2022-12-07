package com.farware.recipesaver.feature_recipe.presentation.appbar

import android.content.Context
import android.provider.CalendarContract
import android.view.MenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Essentially a wrapper around a lambda function to give it a name and icon.
// As an item on the action bar, the action will be displayed with an IconButton
// with the given icon, if not null. Otherwise, the string from the name resource is used.
// In overflow menu, item will always be displayed as text.


// create the action menu with overflow dropdown
// Note: should be used in a RowScope
@Composable
fun ActionMenu(
    items: List<ActionItem>,
    context: Context,
    hasSearchString: Boolean,
    selectFavoritesOnly: Boolean,
    searchSectionVisible: Boolean,
    orderSectionVisible: Boolean
) {
    var menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) }

    val screenWidth = context.resources.displayMetrics.widthPixels.dp
    var numIcons = 3

    if (screenWidth < 500.dp) numIcons = 2

    if (items.isEmpty()) {
        return
    }
    //val state = state
    // decide how many action items to show as icons
    val (appbarActions, overflowActions) = remember(items, numIcons) {
        separateIntoIconAndOverflow(items, numIcons)
    }

    fun dropdownMenuItemClicked(item: ActionItem) {
        menuVisible.value = false
        item.doAction()
    }

    fun dropdownMenuDismissed() {
        menuVisible.value = false
    }

    fun overflowIconButtonClicked() {
        menuVisible.value = true
    }

    MainActionMenuContent(
        appbarActions = appbarActions,
        overflowActions = overflowActions,
        selectFavoritesOnly = selectFavoritesOnly,
        isSearchSectionVisible = searchSectionVisible,
        isOrderSectionVisible = orderSectionVisible,
        menuVisible = menuVisible.value,
        hasSearchString = hasSearchString,
        onDropdownMenuItemClick = { dropdownMenuItemClicked(it)},
        onDropdownMenuDismiss = { dropdownMenuDismissed() },
        onOverflowIconButtonClick = { overflowIconButtonClicked() },
    )


}

@Composable
fun MainActionMenuContent(
    appbarActions: List<ActionItem>,
    overflowActions: List<ActionItem>,
    selectFavoritesOnly: Boolean,
    isSearchSectionVisible: Boolean,
    isOrderSectionVisible: Boolean,
    menuVisible: Boolean,
    hasSearchString: Boolean,
    onDropdownMenuItemClick: (item: ActionItem) -> Unit,
    onDropdownMenuDismiss: () -> Unit,
    onOverflowIconButtonClick: () -> Unit
) {
    for (item in appbarActions) {
        key(item.hashCode()) {
            if (item.icon != null) {
                IconButton(onClick = item.doAction) {
                    //// TODO: Colors all items all
                    if (item.name == "Favorite") {
                        if (selectFavoritesOnly) {
                            Icon(item.icon, item.name, tint = Color.Red)
                        } else {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = .5f))
                        }
                    }
                    if (item.name == "Search") {
                        if (isSearchSectionVisible || hasSearchString) {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = .5f))
                        } else {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint)
                        }
                    }
                    if (item.name == "Time Search") {
                        if (isSearchSectionVisible || hasSearchString) {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = .5f))
                        } else {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint)
                        }
                    }
                    if (item.name == "Sort") {
                        if (isOrderSectionVisible) {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = .5f))
                        } else {
                            Icon(item.icon, item.name, tint = MaterialTheme.colorScheme.surfaceTint)
                        }
                    }
                }
            } else {
                TextButton(onClick = item.doAction) {
                    Text(
                        text = item.name,
                        //TODO: Color
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                        //alpha = LocalContentAlpha.current
                        ),
                    )
                }
            }
        }
    }
    // create the overflow dropdown
    if (overflowActions.isNotEmpty()) {
        IconButton(onClick = {
            onOverflowIconButtonClick()
        }
        ) {
            Icon(Icons.Default.MoreVert, "More actions")
        }
        DropdownMenu(
            expanded = menuVisible,
            onDismissRequest = { onDropdownMenuDismiss() },
        ) {
            for (item in overflowActions) {
                key(item.hashCode()) {
                    DropdownMenuItem(
                        onClick = {
                            onDropdownMenuItemClick(item)
                        },
                        text = {
                            Text(item.name)
                        }
                    )
                }
            }
        }
    }
}

// separate the action items by overflow mode
private fun separateIntoIconAndOverflow(
    items: List<ActionItem>,
    numIcons: Int
): Pair<List<ActionItem>, List<ActionItem>> {
    var (iconCount, overflowCount, preferIconCount) = Triple(0, 0, 0)
    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> iconCount++
            OverflowMode.IF_NECESSARY -> preferIconCount++
            OverflowMode.ALWAYS_OVERFLOW -> overflowCount++
            OverflowMode.NOT_SHOWN -> {}
        }
    }

    val needsOverflow = iconCount + preferIconCount > numIcons || overflowCount > 0
    val actionIconSpace = numIcons - (if (needsOverflow) 1 else 0)

    val iconActions = ArrayList<ActionItem>()
    val overflowActions = ArrayList<ActionItem>()

    var iconsAvailableBeforeOverflow = actionIconSpace - iconCount
    for (item in items) {
        when (item.overflowMode) {
            OverflowMode.NEVER_OVERFLOW -> {
                iconActions.add(item)
            }
            OverflowMode.ALWAYS_OVERFLOW -> {
                overflowActions.add(item)
            }
            OverflowMode.IF_NECESSARY -> {
                if (iconsAvailableBeforeOverflow > 0) {
                    iconActions.add(item)
                    iconsAvailableBeforeOverflow--
                } else {
                    overflowActions.add(item)
                }
            }
            OverflowMode.NOT_SHOWN -> {
                // skip
            }
        }
    }
    return Pair(iconActions, overflowActions)
}