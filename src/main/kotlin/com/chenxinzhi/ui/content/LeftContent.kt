package com.chenxinzhi.ui.content

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import kotlinx.coroutines.delay

@Composable
fun LeftContent() {
    Box(
        modifier = Modifier
            .width(200.dp).fillMaxHeight().background(GlobalStyle.rightColor)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painterResource("image/avatar.jpeg"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                )
                Text(
                    "往事知多少",
                    fontSize = globalStyle.current.avatarFontSize,
                    color = globalStyle.current.avatarFontColor
                )
                Box(modifier = Modifier.width(8.dp))
                Icon(painter = painterResource("image/ic_triangle_right.webp"),
                    contentDescription = null,
                    tint = globalStyle.current.avatarFontRightIconColor,
                    modifier = Modifier
                        .offset { IntOffset(0, 2.dp.roundToPx()) }
                        .size(8.dp)

                )
            }
            val listItem = listOf(
                "发现音乐" to painterResource("image/ic_sound_effect.webp"),
                "播客" to painterResource("image/ic_sound_effect.webp"),
                "私人漫游" to painterResource("image/ic_sound_effect.webp"),
                "视频" to painterResource("image/ic_sound_effect.webp"),
                "关注" to painterResource("image/ic_sound_effect.webp"),
            )
            Box {
                val scrollState = rememberLazyListState()
                LazyColumn(state = scrollState) {

                    val checkItem = 0
                    itemsIndexed(listItem) { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(end = 10.dp)
                                .height(35.dp)
                                .background(
                                    if (index == checkItem) globalStyle.current.leftCheckBackgroundColor else globalStyle.current.leftUnCheckBackgroundColor
                                )
                        ) {
                            Box(modifier = Modifier.width(20.dp))
                            Icon(
                                item.second,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (index == checkItem) globalStyle.current.leftCheckFontColor else globalStyle.current.leftUnCheckFontColor,
                            )
                            Box(modifier = Modifier.width(8.dp))
                            Text(
                                item.first,
                                color = if (index == checkItem) globalStyle.current.leftCheckFontColor else globalStyle.current.leftUnCheckFontColor,
                                fontSize = globalStyle.current.leftFontSize
                            )
                        }
                    }
                    items(50) {
                        Row(modifier = Modifier.fillMaxWidth()) { Text("111") }
                    }

                }
                var show by remember { mutableStateOf(false) }
                var move by remember { mutableStateOf(false) }
                var isFirst by remember { mutableStateOf(true) }
                val rememberScrollbarAdapter = rememberScrollbarAdapter(scrollState)
                LaunchedEffect(
                    scrollState.isScrollInProgress,
                    rememberScrollbarAdapter.scrollOffset
                ) {
                    if (scrollState.isScrollInProgress) {
                        show = true
                    } else {
                        if (isFirst) {
                            isFirst = false
                        } else {
                            show = true
                        }
                        delay(2000)
                        show = false
                    }
                }
                if (show) {
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd)
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    val awaitPointerEvent = awaitPointerEvent()
                                    move = awaitPointerEvent.type == PointerEventType.Move
                                }

                            },
                        adapter = rememberScrollbarAdapter,
                        style = ScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = 8.dp,
                            shape = RoundedCornerShape(4.dp),
                            hoverDurationMillis = 300,
                            unhoverColor = globalStyle.current.scrollColor,
                            hoverColor = globalStyle.current.scrollCheckColor
                        )
                    )
                }

            }

        }
    }
}