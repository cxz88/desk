package com.chenxinzhi.ui.content

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chenxinzhi.route.ContentRoute
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun LeftContent(navigator: Navigator) {
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
                when (checkItem) {
                    0 -> {
                        navigator.navigate(ContentRoute.search)
                    }
                    1 -> {
                        navigator.navigate(ContentRoute.love)
                    }
                    2 -> {
                        navigator.navigate(ContentRoute.download)
                    }
                    else -> {}
                }
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
            // Pick files from Compose


        }
    }
}