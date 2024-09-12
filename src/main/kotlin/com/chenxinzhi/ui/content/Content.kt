package com.chenxinzhi.ui.content

import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.ui.compoent.MediaPlayer
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.topbar.Bar
import com.chenxinzhi.ui.topbar.RightBar

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */

@Composable
fun FrameWindowScope.Content(
    state: WindowState,
    exitApplication: () -> Unit,
    content: @Composable FrameWindowScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
            .border(.01.dp, color = Color(0x33ffffff), RoundedCornerShape(10.dp)).background(
                color = GlobalStyle.backgroundColor
            ).fillMaxWidth()
    ) {
        var show by remember { mutableStateOf(true) }
        Column {
            Box {
                Bar(state, exitApplication)
                Bar(state, exitApplication) {

                        RightBar()

                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
//                content()
                androidx.compose.animation.AnimatedVisibility(show, enter =
                slideIn { IntOffset(0, it.height) }, exit = slideOut { IntOffset(0, it.height) }) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Red)) {}
                }
            }
        }

        //播放器
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            MediaPlayer(javaClass.getResource("/music/M500000SFLv10YFDuo.mp3")?.toURI().toString()) { show = !show }
        }


    }

}





