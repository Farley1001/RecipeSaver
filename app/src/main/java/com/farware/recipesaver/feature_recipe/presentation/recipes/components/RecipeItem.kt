package com.farware.recipesaver.feature_recipe.presentation.recipes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.spacing

@Composable
fun RecipeItem(
    recipe: RecipeWithCategory,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    val categoryColor = Color(recipe.background(isSystemInDarkTheme()))
    val onCategoryColor = Color(recipe.onBackground(isSystemInDarkTheme()))

    RecipeItemContent(
        modifier = modifier,
        recipe = recipe,
        cornerRadius = cornerRadius,
        cutCornerSize = cutCornerSize,
        categoryColor = categoryColor,
        categoryTextColor = onCategoryColor,
        onDeleteClick = onDeleteClick
    )
}

@Composable
fun RecipeItemContent(
    modifier: Modifier,
    recipe: RecipeWithCategory,
    cornerRadius: Dp,
    cutCornerSize: Dp,
    categoryColor: Color,
    categoryTextColor: Color,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = categoryColor,
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
            clipPath(clipPath) {
                // flipped down corner
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(categoryColor.toArgb(), 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .padding(start = 8.dp, end = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Prep " + if(recipe.prepTime!!.toInt() / 60 > 0 || recipe.prepTime!!.toInt() % 60 > 0) {
                        "${recipe.prepTime!!.toInt() / 60}:" + "${recipe.prepTime!!.toInt() % 60}"
                    }
                    //else if(recipe.recipe.prepTime!!.toInt() % 60 > 0) {
                    //    "${recipe.recipe.prepTime!!.toInt() % 60}"
                    //}
                    else "",
                    //TODO: Font Size
                    //style = MaterialTheme.typography.caption,
                    color = categoryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Cook " + if(recipe.cookTime!!.toInt() / 60 > 0) {
                        "${recipe.cookTime!!.toInt() / 60}:" + "${recipe.cookTime!!.toInt() % 60}"
                    }
                    else if(recipe.cookTime!!.toInt() % 60 > 0) {
                        ":${recipe.cookTime!!.toInt() % 60}"
                    } else "",
                    //TODO: Font Size
                    //style = MaterialTheme.typography.caption,
                    color = categoryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    //modifier = Modifier.align(Alignment.End),
                    text = recipe.category,
                    //TODO: Font Size
                    //style = MaterialTheme.typography.caption,
                    color = categoryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                //horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.name,
                    //TODO: Font Size
                    style = MaterialTheme.typography.titleLarge,
                    color = categoryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (recipe.favorite == true) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Is favorite recipe",
                        tint = Color.Red,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(
                text = recipe.description,
                //TODO: Font Size
                //style = MaterialTheme.typography.body1,
                color = categoryTextColor,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = onDeleteClick
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete recipe",
                tint = categoryTextColor,
            )
        }
    }
}