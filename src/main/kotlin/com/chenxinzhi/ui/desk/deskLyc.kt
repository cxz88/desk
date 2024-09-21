package com.chenxinzhi.ui.desk

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerDefaults.shape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.utils.addMove
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun deskLyc(lycContent: MutableStateFlow<String>, lycDeskShow: MutableStateFlow<Boolean>, windowState: WindowState) {
    var nowContent by remember { mutableStateOf("") }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val collectIsHoveredAsState by interactionSource.collectIsHoveredAsState()
    var twoShow by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        twoShow = false
    }
    val bg by animateColorAsState(if (collectIsHoveredAsState || twoShow) globalStyle.current.deskLycBackGround else Color.Transparent)
    val bgBorder by animateColorAsState(if (collectIsHoveredAsState || twoShow) Color(0x22ffffff) else Color.Transparent)
    val ap by animateFloatAsState(if (collectIsHoveredAsState || twoShow) 1f else 0f)
    Box(modifier = Modifier
        .border(0.001.dp, bgBorder, RoundedCornerShape(8.dp))
        .graphicsLayer {
            shape = RoundedCornerShape(8.dp)
            clip = true
        }
        .background(color = bg)
        .addMove(windowState)
        .fillMaxSize()
        .hoverable(interactionSource),
        contentAlignment = Alignment.Center

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.offset(6.dp, 6.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        lycDeskShow.value = false
                    }
                }
                .size(24.dp)) {
                val imageBitmap =
                    javaClass.getResourceAsStream("/image/ic_window_close.webp")?.use { loadImageBitmap(it) }
                imageBitmap?.let {
                    drawImage(
                        it,
                        dstSize = IntSize(24.dp.roundToPx(), 24.dp.roundToPx()),
                        alpha = ap,
                    )
                }
            }
        }


        Text(nowContent, color = Color.White, fontSize = 20.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)
        LaunchedEffect(Unit) {
            lycContent.collect {
                nowContent = it
            }
        }
    }

}