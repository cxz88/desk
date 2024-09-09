package com.chenxinzhi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.topbar.Bar

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */

@Composable
fun FrameWindowScope.AppContent(
    state: WindowState,
    exitApplication: () -> Unit,
    content: @Composable FrameWindowScope.() -> Unit={}
) {
    Box(
        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
            .border(.01.dp, color = Color(0x33ffffff), RoundedCornerShape(10.dp)).background(
                color = GlobalStyle.backgroundColor
            ).fillMaxWidth()
    ) {
        Column {
            Bar(state, exitApplication)
            content()
        }


    }

}





