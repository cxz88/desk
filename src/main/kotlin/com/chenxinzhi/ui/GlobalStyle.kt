package com.chenxinzhi.ui


import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */
val globalStyle = compositionLocalOf { GlobalStyle }

object GlobalStyle {
    var backgroundColor by mutableStateOf(Color(0xff2b2b2b))
    var backgroundTopLeftColor by mutableStateOf(Color(0xff2d2d2d))
    var textUnCheckColor by mutableStateOf(Color(0xff878787))
    var textCheckColor by mutableStateOf(Color(0xffFFFFFF))
    val defaultFontSize by mutableStateOf( 14.sp)
    val defaultSearchFontSize by mutableStateOf( 12.sp)
    val searchColor by mutableStateOf( Color(0xff4b4b4b))
    val searchIconColor by mutableStateOf( Color(0x66FFFFFF))
    val topBarRightColor by mutableStateOf( Color(0xff777777))
}
