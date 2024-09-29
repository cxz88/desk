package com.chenxinzhi.ui.content

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.style.TextAlign
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
            val listItem = listOf(
                "发现音乐",
                "我的喜欢",
                "下载列表",
            )
            Box {
                val scrollState = rememberLazyListState()
                var checkItem by remember { mutableStateOf(0) }
                LazyColumn(state = scrollState) {
                    itemsIndexed(listItem) { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable(remember { MutableInteractionSource() }, indication = null) {
                                    checkItem=index
                                }
                                .height(35.dp)
                                .background(
                                    if (index == checkItem) globalStyle.current.leftCheckBackgroundColor else globalStyle.current.leftUnCheckBackgroundColor
                                )
                        ) {
                            Text(
                                item,
                                color = if (index == checkItem) globalStyle.current.leftCheckFontColor else globalStyle.current.leftUnCheckFontColor,
                                fontSize = globalStyle.current.leftFontSize
                            )
                        }
                    }

                }

            }

        }
    }
}