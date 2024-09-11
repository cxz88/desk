package com.chenxinzhi.ui.style


import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
    val rightColor by mutableStateOf( Color(0xff202020))
    val contentBackgroundColor by mutableStateOf( Color(0xff252525))
    val scrollColor by mutableStateOf( Color(0xff404040))
    val scrollCheckColor by mutableStateOf( Color(0xff484848))
    val avatarFontSize by mutableStateOf( 14.sp)
    val avatarFontColor by mutableStateOf( Color(0xffb1b1b1))
    val leftFontSize by mutableStateOf( 14.sp)
    val avatarFontRightIconColor by mutableStateOf( Color(0xff636363))
    val leftCheckBackgroundColor by mutableStateOf( Color(0xff1b1b1b))
    val leftUnCheckBackgroundColor by mutableStateOf( Color(0xff202020))
    val leftCheckFontColor by mutableStateOf( Color(0xffc93830))
    val leftUnCheckFontColor by mutableStateOf( Color(0xffb1b1b1))
    val mediaPlayerBackgroundColor by mutableStateOf(Color(0xff252525))
    val mediaPlayerProcessColor by mutableStateOf(Color(0xffd33a31))
    val mediaPlayerMusicNameSize by mutableStateOf(13.sp)
    val mediaPlayerMusicNameColor by mutableStateOf(Color(0xffbebebe))
    val mediaPlayerMusicSingerNameSize by mutableStateOf(11.sp)
    val mediaPlayerMusicSingerNameColor by mutableStateOf(Color(0xff939393))
    val mediaPlayerMusicSingerNameCheckColor by mutableStateOf(Color(0xff252525))
    val durationColor by mutableStateOf(Color(0xff626262))
    val durationFontSize by mutableStateOf(13.sp)
}
