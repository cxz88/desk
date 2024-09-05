package com.chenxinzhi.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimary
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import java.awt.Component
import java.awt.event.MouseEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FrameWindowScope.App(
    state: WindowState,
    exitApplication: () -> Unit,
    content: @Composable FrameWindowScope.() -> Unit
) {
    Box(
        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
            .border(.01.dp, color = Color(0x33ffffff), RoundedCornerShape(10.dp)).background(
                color = GlobalStyle.backgroundColor
            ).fillMaxSize()
    ) {
        Column {
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
                                    if (awaitPointerEvent.button.isPrimary) {
                                        val nativeEvent = awaitPointerEvent.nativeEvent
                                        if (nativeEvent is MouseEvent) {
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
                val min by animateColorAsState(
                    targetValue = if (active) {
                        Color(0xfffdbb2e)
                    } else {
                        Color(0xFF53636a)
                    }
                )
                Row {
                    Row(
                        modifier = Modifier.fillMaxHeight().width(200.dp)
                            .background(GlobalStyle.backgroundTopLeftColor).padding(
                                end = 10.dp, bottom = 10.dp
                            )
                    ) {
                        Row(modifier = Modifier.width(100.dp).padding(15.dp, 12.dp, 0.dp, 0.dp)) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(6.0.dp))
                                    .background(color = close).size(12.dp).hoverable(
                                        m
                                    ).clickable { exitApplication() },
                                contentAlignment = Alignment.Center
                            ) {
                                if (hover) {
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
                                    .background(color = min).size(12.dp).hoverable(
                                        m
                                    ).clickable { window.isMinimized = true; },
                                contentAlignment = Alignment.Center
                            ) {
                                if (hover) {
                                    Box(modifier = Modifier.size(6.dp, 1.dp).background(Color.Black)) {
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.width(40.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            Icon(
                                painterResource("image/ic_back.webp"),
                                contentDescription = null,
                                tint = Color(0xFFaeaeae),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                painterResource("image/ic_more.webp"),
                                contentDescription = null,
                                tint = Color(0xFFaeaeae),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }
                    Row(
                        modifier = Modifier.offset { IntOffset(0, -5.dp.roundToPx()) }.width(400.dp).fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "个性推荐",
                            color = GlobalStyle.textCheckColor,
                            fontSize = GlobalStyle.defaultFontSize
                        )
                        Text(
                            "歌单",
                            color = GlobalStyle.textUnCheckColor,
                            fontSize = GlobalStyle.defaultFontSize
                        )
                        Text(
                            "排行榜",
                            color = GlobalStyle.textUnCheckColor,
                            fontSize = GlobalStyle.defaultFontSize
                        )
                        Text(
                            "歌手",
                            color = GlobalStyle.textUnCheckColor,
                            fontSize = GlobalStyle.defaultFontSize
                        )
                        Text(
                            "最新音乐",
                            color = GlobalStyle.textUnCheckColor,
                            fontSize = GlobalStyle.defaultFontSize
                        )
                    }
                    Row(
                        modifier = Modifier.offset { IntOffset(0, 0) }.weight(1f).fillMaxHeight(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom

                    ) {
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(0, -5.dp.roundToPx()) }.clip(shape = RoundedCornerShape(16.dp))
                                .width(150.dp).height(25.dp).background(globalStyle.current.searchColor)
                        ) {
                            Icon(Icons.Default.Search,
                                contentDescription = null,
                                tint = globalStyle.current.searchIconColor,
                                modifier = Modifier
                                    .offset { IntOffset(4.dp.roundToPx(), 4.dp.roundToPx()) }
                                    .size(20.dp))
                            Text(
                                "搜索",
                                color = globalStyle.current.textUnCheckColor,
                                fontSize = GlobalStyle.defaultSearchFontSize,
                                modifier = Modifier.offset { IntOffset(25.dp.roundToPx(), 0) }
                            )

                        }
                        Box(modifier = Modifier.width(10.dp))
                        Row(modifier = Modifier.offset { IntOffset(0,-6.dp.roundToPx()) }.fillMaxHeight(), verticalAlignment = Alignment.Bottom) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = globalStyle.current.topBarRightColor,
                                modifier = Modifier.size(19.dp)
                            )
                            Box(modifier = Modifier.width(16.dp))
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = null,
                                tint = globalStyle.current.topBarRightColor,
                                modifier = Modifier.size(19.dp)
                            )
                            Box(modifier = Modifier.width(16.dp))
                            Icon(
                                painterResource("image/ic_theme.webp"),
                                contentDescription = null,
                                tint = globalStyle.current.topBarRightColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Box(modifier = Modifier.width(16.dp))
                            Icon(
                                painterResource("image/ic_screen_max.webp"),
                                contentDescription = null,
                                tint = globalStyle.current.topBarRightColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Box(modifier = Modifier.width(16.dp))
                        }
                    }
                }


            }
            content()
        }


    }

}