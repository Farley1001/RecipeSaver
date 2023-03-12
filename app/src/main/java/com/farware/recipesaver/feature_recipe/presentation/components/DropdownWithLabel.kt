package com.farware.recipesaver.feature_recipe.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.presentation.recipe.components.TextWithAppendedContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownWithLabel(
    labelText: String,
    initialIndex: Int,
    items: List<Category>,
    onDropdownItemSelected: (Long) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuIndex by remember { mutableStateOf(initialIndex) }

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = labelText,
                // TODO: Font Size and color
                //fontSize = 12.sp,
                //color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .height(30.dp)
                .clickable(
                    onClick = { showMenu = true }
                ),
            horizontalArrangement = Arrangement.Start
        ) {
            TextWithAppendedContent(
                text = items[menuIndex].name,
                onTextClicked = { showMenu = true },
                placeholderWidth = 24.sp,
                placeholderHeight = 24.sp,
                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                appendContent = {
                    Badge(
                        modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                        containerColor = Color(items[menuIndex].background(isSystemInDarkTheme()))
                    ){
                        Text(text = "")
                    }
                }
            )
            Icon(
                contentDescription = "local content description",
                imageVector = Icons.Outlined.ExpandMore,
                // TODO: Color
                //tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 100.dp)
                    .size(48.dp)
                    .rotate(
                        animateFloatAsState(
                            if (!showMenu) 0f else 180f,
                        ).value
                    )
                    .align(Alignment.Top)
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.fillMaxWidth(),
            ) {
                items.forEachIndexed { index, _ ->
                    DropdownMenuItem(
                        text = {
                            TextWithAppendedContent(
                                text = items[index].name,
                                onTextClicked = {
                                    menuIndex = index
                                    showMenu = false
                                    onDropdownItemSelected(items[index].categoryId!!)
                                },
                                placeholderWidth = 24.sp,
                                placeholderHeight = 24.sp,
                                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                                appendContent = {
                                    Badge(
                                        modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                                        containerColor = Color(items[index].background(isSystemInDarkTheme()))
                                    ){
                                        Text(text = "")
                                    }
                                }
                            )
                        },
                        trailingIcon = {
                            if(items[index].name == items[menuIndex].name) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Localized content description", tint = Color.Green.copy(alpha = .5f))
                            } else { }
                        },
                        onClick = {
                            menuIndex = index
                            showMenu = false
                            onDropdownItemSelected(items[index].categoryId!!)
                        },
                        modifier =
                        if(items[index].name == items[menuIndex].name) {
                            Modifier
                                .background(MaterialTheme.colorScheme.background.copy(alpha = .3f))
                                .wrapContentSize(Alignment.BottomEnd)
                        } else {
                            Modifier.wrapContentSize(Alignment.BottomEnd)
                        }

                    )
                }
            }
        }
    }
}