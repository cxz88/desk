package com.chenxinzhi.ui.topbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import java.awt.Component
import java.awt.event.MouseEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FrameWindowScope.Bar(
    state: WindowState,
    exitApplication: () -> Unit,
    rightContent: @Composable (RowScope.(
    ) -> Unit)? = null,
    leftContent: @Composable (RowScope.(
    ) -> Unit)? = null
) {
    Box(
        modifier = Modifier.background(
            Color.Transparent
        ).fillMaxWidth()
            .pointerInput(Unit) {
                        awaitPointerEventScope {
                            var preXOnScreen = 0
                            var preYOnScreen = 0
                            var startX = 0
                            var startY = 0
                            var posX = 0.dp
                            var posY = 0.dp
                            while (true) {
                                val awaitPointerEvent = awaitPointerEvent()
                                if (awaitPointerEvent.changes[0].isConsumed) {
                                    continue
                                }
                                if (awaitPointerEvent.type == PointerEventType.Press) {
                                    val ne = awaitPointerEvent.nativeEvent
                                    if (ne is MouseEvent) {
                                        preXOnScreen = ne.xOnScreen
                                        preYOnScreen = ne.yOnScreen
                                        val component = ne.source as Component
                                        startX = component.x
                                        startY = component.y
                                        val position = state.position
                                        posX = position.x
                                        posY = position.y
                                    }
                                } else if (awaitPointerEvent.type == PointerEventType.Move) {
                                    val nativeEvent = awaitPointerEvent.nativeEvent
                                    if (nativeEvent is MouseEvent) {
                                        if (nativeEvent.modifiersEx != MouseEvent.BUTTON1_DOWN_MASK) {
                                            continue
                                        }
                                        val xOnScreen = nativeEvent.xOnScreen
                                        val yOnScreen = nativeEvent.yOnScreen
                                        val xOffset = xOnScreen - preXOnScreen
                                        val yOffset = yOnScreen - preYOnScreen
                                        state.position =
                                            WindowPosition(
                                                posX + xOffset.sp.toDp() + startX.sp.toDp(),
                                                posY + yOffset.sp.toDp() + startY.sp.toDp()
                                            )
                                    }


                                }
                            }
                        }


            }
            .height(50.dp)
    ) {
        val m = remember { MutableInteractionSource() }
        var active by remember { mutableStateOf(true) }
        val hover by m.collectIsHoveredAsState()
        window.addWindowListener(object : WindowListener {
            override fun windowOpened(e: WindowEvent?) {

            }

            override fun windowClosing(e: WindowEvent?) {

            }

            override fun windowClosed(e: WindowEvent?) {

            }

            override fun windowIconified(e: WindowEvent?) {

            }

            override fun windowDeiconified(e: WindowEvent?) {

            }

            override fun windowActivated(e: WindowEvent?) {
                active = true
            }

            override fun windowDeactivated(e: WindowEvent?) {
                active = false
            }

        })

        val close by animateColorAsState(
            targetValue = if (active) {
                Color(0xffff5f57)
            } else {
                Color(0xFF53636a)
            }
        )
        val minColor by animateColorAsState(
            targetValue = if (active) {
                Color(0xfffdbb2e)
            } else {
                Color(0xFF53636a)
            }
        )
        Row {
            LeftBar(close, m, exitApplication, hover, minColor) {
                leftContent?.let {
                    it()
                }
            }
            rightContent?.let {
                it()
            }
        }


    }
}