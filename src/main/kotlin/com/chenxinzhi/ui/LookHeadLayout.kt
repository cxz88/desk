package com.chenxinzhi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LookaheadScope

@Composable
fun LookHeadLayout(modifier: Modifier = Modifier, content: @Composable LookaheadScope.() -> Unit) {

    Layout(content = {
        LookaheadScope {
            content()
        }

    }, modifier = modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val height = placeables.maxOf { it.height }
        val width = placeables.maxOf { it.width }
        layout(width, height) {
            placeables.forEachIndexed { _, placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }

}
