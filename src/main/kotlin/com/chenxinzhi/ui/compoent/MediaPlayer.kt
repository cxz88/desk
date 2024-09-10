package com.chenxinzhi.ui.compoent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chenxinzhi.ui.style.globalStyle
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/9
 */
@Composable
fun MediaPlayer() {
    Box(
        modifier = Modifier.background(globalStyle.current.mediaPlayerBackgroundColor).fillMaxWidth()
            .height(62.dp)
    ) {
        MusicLinearProgressIndicator()
    }
}

@Composable
fun MusicLinearProgressIndicator() {
    BoxWithConstraints {
        val processWidth = constraints.maxWidth
        var process by remember { mutableStateOf(0f) }
        val processWidthUse = min(processWidth * process, processWidth.toFloat())
        val mediaPlayerProcessColor = globalStyle.current.mediaPlayerProcessColor
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(2.dp)

        ) {
            drawRect(mediaPlayerProcessColor, size = Size(processWidthUse, size.height))
        }
        val interactionSource = remember { MutableInteractionSource() }
        val hover by interactionSource.collectIsHoveredAsState()
        val alpha by animateFloatAsState(if (hover) 1f else 0f)
        Canvas(
            Modifier
                .offset {
                    val fl = 12.dp.toPx() / 2 - 1
                    IntOffset(-fl.roundToInt() + processWidthUse.roundToInt(), -fl.roundToInt())
                }
                .draggable(rememberDraggableState { delta ->
                    process += delta / processWidth
                    println(process)

                }, Orientation.Horizontal)
                .size(12.dp)
                .hoverable(interactionSource).alpha(alpha)
        ) {
            drawCircle(mediaPlayerProcessColor)
        }

    }
}