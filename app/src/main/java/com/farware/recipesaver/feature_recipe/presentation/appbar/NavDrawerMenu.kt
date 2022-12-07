package com.farware.recipesaver.feature_recipe.presentation.appbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun NavDrawerMenu(
    selectedMenuItem: String,
    onNavButtonCloseClick: (String) -> Unit
) {
    val items = listOf(
        NavMenuItem(
            name = "Recipes",
            route = "recipes_screen",
            icon = Icons.Default.FactCheck,
            contentDescription = "Navigate to recipes screen."
        ),
        NavMenuItem(
            name = "Categories",
            route = "categories_screen",
            icon = Icons.Default.Category,
            contentDescription = "Navigate to categories screen."
        ),
        NavMenuItem(
            name = "Settings",
            route = "settings_screen",
            icon = Icons.Default.Settings,
            contentDescription = "Navigate to settings screen."
        ),
        NavMenuItem(
            name = "Sign Out",
            route = "sign_out",
            icon = Icons.Default.Logout,
            contentDescription = "Sign out navigation option.",
            hasDivider = true
        )
    )

    NavDrawerMenuContent(
        items = items,
        selectedItem = selectedMenuItem,
        onNavButtonCloseClick = { onNavButtonCloseClick(it) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawerMenuContent(
    items: List<NavMenuItem>,
    selectedItem: String,
    onNavButtonCloseClick: (String) -> Unit,
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp)) {
            Text(
                text = "My Recipes",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            items.forEach { item ->
                if (item.hasDivider) Divider()
                NavigationDrawerItem(
                    icon = { Icon(item.icon!!, contentDescription = item.contentDescription) },
                    label = { Text(item.name) },
                    selected = item.route == selectedItem,
                    onClick = {
                        onNavButtonCloseClick(item.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}