package com.chenxinzhi.ui.content

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chenxinzhi.ui.Swipe
import com.chenxinzhi.ui.style.globalStyle

@Composable
fun RightContent() {
    Box {
        val scrollState = rememberLazyListState()
        LazyColumn(state = scrollState) {
            item {
                Surface(
                    modifier = Modifier
                        .background(globalStyle.current.contentBackgroundColor)
                        .padding(horizontal = 20.dp)
//                        .graphicsLayer {
//                            renderEffect= BlurEffect(11f,11f, TileMode.Repeated)
//                            clip = true
//                        }
            ,
                    color = globalStyle.current.contentBackgroundColor
                ) {
                    Swipe()

                }
            }
            item {
            }
            items(20) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text("第$it")
                    Box(modifier = Modifier.height(100.dp))
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(scrollState),
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