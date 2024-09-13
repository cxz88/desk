package com.chenxinzhi.ui.style


import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */
val globalStyle = compositionLocalOf { GlobalStyle }

object GlobalStyle {
    var backgroundColor by mutableStateOf(Color(0xff2b2b2b))
    var backgroundTopLeftColorChange by mutableStateOf(Color(0xff2d2d2d))

    val backgroundTopLeftColor
        @Composable
        get()=animateColorAsState(backgroundTopLeftColorChange)
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
    val mediaPlayerMusicNameSize by mutableStateOf(14.sp)
    val mediaPlayerMusicNameColor by mutableStateOf(Color(0xffbebebe))
    val mediaPlayerMusicSingerNameSize by mutableStateOf(12.sp)
    val mediaPlayerMusicSingerNameColor by mutableStateOf(Color(0xff939393))
    val mediaPlayerMusicSingerNameCheckColor by mutableStateOf(Color(0xff252525))
    val durationColor by mutableStateOf(Color(0xff626262))
    val musicControlColor by mutableStateOf(Color(0xffd33a31))
    val CoverShadow by mutableStateOf(Color(0xff282e2d))
    val lycColor by mutableStateOf(Color(0xff8b8b8b))
    val lycFontSize by mutableStateOf(16f)
    val lycRightColor by mutableStateOf(Color(0xff333232))
    val lycCheckColor by mutableStateOf(Color(0xffffffff))
    val lycFontCheckSize by mutableStateOf(17f)
    val durationFontSize by mutableStateOf(13.sp)
}
