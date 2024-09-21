package com.chenxinzhi.ui.topbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.utils.addMove
import java.awt.event.WindowEvent
import java.awt.event.WindowListener


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
            .addMove(state)
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