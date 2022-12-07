package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownWithLabel(
    labelText: String,
    initialIndex: Int,
    items: List<CategoryWithColor>,
    onDropdownItemSelected: (Long) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuIndex by remember { mutableStateOf(initialIndex) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = labelText,
            // TODO: Font Size and color
            //fontSize = 12.sp,
            //color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
        Row(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .clickable(
                    onClick = { showMenu = true }
                )
        ) {
            /*Text(
                // TODO: selected category in text
                text = items[menuIndex].category.name,
                // TODO: Font Size and color
                //fontSize = 20.sp,
                //color = Color.Black,
                modifier = Modifier
                    .wrapContentSize()
                    // TODO: Color
                    //.background(Color.White)       //.background(Color(items[menuIndex].color))
            )*/
            TextWithAppendedContent(
                text = items[menuIndex].category.name,
                onTextClicked = { showMenu = true },
                placeholderWidth = 24.sp,
                placeholderHeight = 24.sp,
                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                appendContent = {
                    Badge(
                        modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                        containerColor = Color(items[menuIndex].categoryColor.background(isSystemInDarkTheme()))
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
                                text = items[index].category.name,
                                onTextClicked = {
                                    menuIndex = index
                                    showMenu = false
                                    onDropdownItemSelected(items[index].category.categoryId!!)
                                },
                                placeholderWidth = 24.sp,
                                placeholderHeight = 24.sp,
                                placeholderVertAlign = PlaceholderVerticalAlign.TextTop,
                                appendContent = {
                                    Badge(
                                        modifier = Modifier.padding(start = 5.dp, top = 1.dp),
                                        containerColor = Color(items[index].categoryColor.background(isSystemInDarkTheme()))
                                    ){
                                        Text(text = "")
                                    }
                                }
                            )
                        },
                        trailingIcon = {
                            if(items[index].category.name == items[menuIndex].category.name) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Localized content description", tint = Color.Green.copy(alpha = .5f))
                            } else { }
                        },
                        onClick = {
                            menuIndex = index
                            showMenu = false
                            onDropdownItemSelected(items[index].category.categoryId!!)
                        },
                        modifier =
                        if(items[index].category.name == items[menuIndex].category.name) {
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