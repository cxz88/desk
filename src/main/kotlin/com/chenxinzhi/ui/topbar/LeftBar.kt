package com.chenxinzhi.ui.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.chenxinzhi.ui.style.GlobalStyle

@Composable
fun FrameWindowScope.LeftBar(
    closeColor: Color,
    mutableInteractionSource: MutableInteractionSource,
    exitApplication: () -> Unit,
    isHover: Boolean,
    minColor: Color,
    content:@Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxHeight().width(200.dp)
            .background(GlobalStyle.backgroundTopLeftColor.value).padding(
                end = 10.dp, bottom = 10.dp
            ).padding(bottom = 6.dp)
    ) {
        Row(modifier = Modifier

            .width(100.dp).padding(15.dp, 12.dp, 0.dp, 0.dp)) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(6.0.dp))
                    .background(color = closeColor).size(12.dp).hoverable(
                        mutableInteractionSource
                    )
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val awaitPointerEvent = awaitPointerEvent()
                                awaitPointerEvent.changes[0].consume()
                            }
                        }
                    }.clickable { exitApplication() },
                contentAlignment = Alignment.Center
            ) {
                if (isHover) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = Color.Black
                    )
                }
            }
            Box(modifier = Modifier.size(8.dp)) {}
            Box(
                modifier = Modifier.clip(RoundedCornerShape(6.0.dp))
                    .background(color = minColor).size(12.dp).hoverable(
                        mutableInteractionSource
                    )
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val awaitPointerEvent = awaitPointerEvent()
                                awaitPointerEvent.changes[0].consume()
                            }
                        }
                    }.clickable { window.isMinimized = true; },
                contentAlignment = Alignment.Center
            ) {
                if (isHover) {
                    Box(modifier = Modifier.size(6.dp, 1.dp).background(Color.Black)) {
                    }
                }
            }
        }
        Box(modifier = Modifier.width(40.dp))
        content()


    }
}