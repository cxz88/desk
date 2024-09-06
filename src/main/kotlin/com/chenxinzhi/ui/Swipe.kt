package com.chenxinzhi.ui


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun Swipe() {
    val state by remember { mutableStateOf(SwipeState(5)) }
    Box {
        extracted(listOf("image/1.jpg", "image/2.jpg", "image/3.jpg", "image/4.jpg", "image/5.jpg"), state)
    }

}


@Composable
private fun extracted(imageList: List<String>, state: SwipeState) {
    val nowIndex = state.index
    LaunchedEffect(Unit) {
        while (true) {
            if (state.index == imageList.size - 1) {
                state.setSwipeIndex(0)
            } else {
                state.setSwipeIndex(state.index + 1)
            }
            delay(3000)
        }
    }
    Column {
        Row {
            Box(
                modifier = Modifier.height(250.dp).width(750.dp).padding(top = 20.dp, bottom = 20.dp),
            ) {
                imageList.forEachIndexed { index, item ->

                    val zIndex by animateFloatAsState(
                        if (nowIndex == index) 888f
                        else if (nowIndex == 1 && index == 0) 777f
                        else if (nowIndex == imageList.size - 1 && index == 0) 777f
                        else if (nowIndex == imageList.size - 2 && index == 0) 555f
                        else if (nowIndex == imageList.size - 1 && index == 1) 666f
                        else if (nowIndex == index - 1) 777f else 666f,
                        tween(state.duration)
                    )
                    val intOffset by animateIntOffsetAsState(
                        IntOffset(
                            with(LocalDensity.current) {
                                if (nowIndex == index) 100.dp.roundToPx() else if (nowIndex == index - 1) 350.dp.roundToPx()
                                else if (nowIndex == 0 && index == imageList.size - 1) 0
                                else if (nowIndex == index + 1) 0 else if (nowIndex == index - 2) 450.dp.roundToPx()
                                else if (nowIndex == imageList.size - 1 && index == 0) 350.dp.roundToPx() else if (nowIndex == imageList.size - 1 && index == 2) 450.dp.roundToPx()
                                else -100.dp.roundToPx()
                            },
                            0
                        ), tween(state.duration)
                    )
                    val paddingV by animateDpAsState(if (nowIndex == index) 0.dp else 25.dp, tween(state.duration))
                    val widthV by animateDpAsState(if (nowIndex == index) 550.dp else 400.dp, tween(state.duration))
                    Image(
                        painterResource(item),
                        modifier = Modifier
                            .zIndex(zIndex)
                            .offset {
                                intOffset
                            }
                            .fillMaxHeight()
                            .padding(top = paddingV, bottom = paddingV)
                            .width(widthV)
                            .background(globalStyle.current.contentBackgroundColor)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                }

            }
            Box(modifier = Modifier.background(globalStyle.current.contentBackgroundColor).height(250.dp).fillMaxWidth()) {}
        }


    }
}

class SwipeState(itemCount: Int) {
    private var itemCountIndex by mutableStateOf(itemCount)
    var index by mutableStateOf(0)
        private set

    fun setSwipeIndex(index: Int) {
        this.index = min(kotlin.math.max(0, index), itemCountIndex - 1)
    }

    var duration by mutableStateOf(300)
}
