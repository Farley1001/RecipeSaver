package com.farware.recipesaver.feature_recipe.presentation.recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun TextWithAppendedContent(
    text: String = "",
    onTextClicked: () -> Unit,
    placeholderWidth: TextUnit,
    placeholderHeight: TextUnit,
    placeholderVertAlign: PlaceholderVerticalAlign,
    appendContent: @Composable () -> Unit
) {
    val myId = "inlineContent"
    val baseText = buildAnnotatedString {
        append(text)
        // Append a placeholder string "[icon]" and attach an annotation "inlineContent" on it.
        appendInlineContent(myId, "[icon]")
    }

    val inlineContent = mapOf(
        Pair(
            // This tells the [CoreText] to replace the placeholder string "[icon]" by
            // the composable given in the [InlineTextContent] object.
            myId,
            InlineTextContent(
                // Placeholder tells text layout the expected size and vertical alignment of
                Placeholder(
                    width = placeholderWidth,
                    height = placeholderHeight,
                    placeholderVerticalAlign = placeholderVertAlign
                )
            ) {
                appendContent()
            }
        )
    )
    Text(
        text = baseText,
        modifier = Modifier
            .padding(start = 8.dp, top = 3.dp)
            .clickable { onTextClicked() },
        inlineContent = inlineContent
    )
}