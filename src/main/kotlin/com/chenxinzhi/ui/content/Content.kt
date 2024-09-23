package com.chenxinzhi.ui.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.sqlservice.FuncEnum
import com.chenxinzhi.sqlservice.getByKey
import com.chenxinzhi.sqlservice.updateByKey
import com.chenxinzhi.ui.compoent.MediaPlayer
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.topbar.Bar
import com.chenxinzhi.ui.topbar.RightBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */

@Composable
fun FrameWindowScope.Content(
    state: WindowState,
    exitApplication: () -> Unit,
    lycContent: MutableStateFlow<String>,
    lycDeskShow: MutableStateFlow<Boolean>,
    searchKey: MutableStateFlow<String>,
    closeFlow: MutableStateFlow<Boolean>,
    content: @Composable (FrameWindowScope.(musicId: MutableStateFlow<String>) -> Unit) = {}
) {
    val current = LocalFocusManager.current
    Box(
        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
            .border(.01.dp, color = Color(0x33ffffff), RoundedCornerShape(10.dp)).background(
                color = GlobalStyle.backgroundColor
            ).fillMaxWidth().pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val awaitPointerEvent = awaitPointerEvent(PointerEventPass.Final)
                        if (!awaitPointerEvent.changes[0].isConsumed && awaitPointerEvent.type == PointerEventType.Press) {
                            current.clearFocus()

                        }
                    }
                }

            }


    ) {
        var show by remember { mutableStateOf(true) }
        GlobalStyle.backgroundTopLeftColorChange = if (show) {
            Color(0xff2b2b2b)
        } else {
            Color(0xff2d2d2d)
        }
        var process by remember { mutableStateOf(0f) }
        val conRate by remember {
            derivedStateOf {
                13 * process
            }
        }

        var isPlay by remember { mutableStateOf(false) }
        val conRateAni by animateFloatAsState(if (isPlay) conRate else -40f)
        var currentTime by remember { mutableStateOf(0f) }
        val searchList = remember { MutableStateFlow(listOf<String>()) }
        val searchTipShow = remember { MutableStateFlow(false) }
        //从数据库中价值
        val musicId = remember {
            MutableStateFlow(runBlocking {
                getByKey(FuncEnum.MUSIC_ID, ",,,")
            })
        }


        Column(horizontalAlignment = Alignment.End) {
            Box {
                Bar(state, exitApplication, rightContent = {
                    RightBar(searchList, searchTipShow, searchKey, {
                        show = false
                    }) {
                        androidx.compose.animation.AnimatedVisibility(
                            !show, enter =
                            fadeIn(), exit = fadeOut()
                        ) {
//                            Row(
//                                modifier = Modifier.offset { IntOffset(0, -5.dp.roundToPx()) }.width(400.dp)
//                                    .fillMaxHeight()
//                                    .padding(bottom = 6.dp),
//                                horizontalArrangement = Arrangement.SpaceEvenly,
//                                verticalAlignment = Alignment.Bottom
//                            ) {
//                                Text(
//                                    "个性推荐",
//                                    color = GlobalStyle.textCheckColor,
//                                    fontSize = GlobalStyle.defaultFontSize
//
//                                )
//                                Text(
//                                    "歌单",
//                                    color = GlobalStyle.textUnCheckColor,
//                                    fontSize = GlobalStyle.defaultFontSize
//                                )
//                                Text(
//                                    "排行榜",
//                                    color = GlobalStyle.textUnCheckColor,
//                                    fontSize = GlobalStyle.defaultFontSize
//                                )
//                                Text(
//                                    "歌手",
//                                    color = GlobalStyle.textUnCheckColor,
//                                    fontSize = GlobalStyle.defaultFontSize
//                                )
//                                Text(
//                                    "最新音乐",
//                                    color = GlobalStyle.textUnCheckColor,
//                                    fontSize = GlobalStyle.defaultFontSize
//                                )
//                            }
                        }

                    }
                }
                ) {
                    androidx.compose.animation.AnimatedVisibility(

                        !show, Modifier.weight(1f).fillMaxHeight(), enter =
                        fadeIn(), exit = fadeOut()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.fillMaxWidth()
                        ) {
//                            Icon(
//                                painterResource("image/ic_back.webp"),
//                                contentDescription = null,
//                                tint = Color(0xFFaeaeae),
//                                modifier = Modifier.size(16.dp)
//                            )
//                            Icon(
//                                painterResource("image/ic_more.webp"),
//                                contentDescription = null,
//                                tint = Color(0xFFaeaeae),
//                                modifier = Modifier.size(16.dp)
//                            )
                        }
                    }

                }
            }
            PlayContent(
                content,
                show,
                isPlay,
                currentTime,
                conRateAni,
                lycContent,
                searchList,
                searchTipShow,
                searchKey,
                musicId
            ) { show = false }

        }
        val md by musicId.collectAsState()
        val ref = remember {
            MutableStateFlow(System.currentTimeMillis())
        }
        LaunchedEffect(md) {
            updateByKey(FuncEnum.MUSIC_ID, md)
            if (md != ",,," && md.split(",").last() != "%%end1996888888888%%") {
                val byKey = getByKey(FuncEnum.PLAY_LIST, "")
                if (byKey.isBlank()) {
                    updateByKey(FuncEnum.PLAY_LIST, md)
                    ref.value=System.currentTimeMillis()
                }else{
                    val filter = byKey.split(":%%19969685426854***").filter { it.split(",")[0] != md.split(",")[0] }
                    if (filter.isEmpty()) {
                        updateByKey(FuncEnum.PLAY_LIST, md)
                        ref.value=System.currentTimeMillis()
                    }else{
                        updateByKey(FuncEnum.PLAY_LIST, "${filter.joinToString(":%%19969685426854***")}:%%19969685426854***$md")
                        ref.value=System.currentTimeMillis()
                    }
                }



            }

        }
        //播放器
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            MediaPlayer(
                md,
                musicId,
                ref,
                lycDeskShow,
                isPlayCallback = {
                    isPlay = it
                },
                processCallback = {
                    process = it
                },
                currentTimeChange = {
                    currentTime = it
                }, closeFlow = closeFlow
            ) { show = !show }
        }

    }

}







