package com.chenxinzhi.ui.content

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowState
import com.chenxinzhi.ui.compoent.MediaPlayer
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.ui.topbar.Bar
import com.chenxinzhi.ui.topbar.RightBar
import com.chenxinzhi.utils.convertToSeconds
import kotlin.math.max

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
    val lycList = remember {
        val lyc =
            "[00:00.00] 作词 : 潘云安\n[00:01.00] 作曲 : 潘云安\n[00:02.00] 编曲 : 告五人\n[00:18.23]\n[00:46.82]形同虚设的时间\n[00:50.32]在你眼里成为了无限\n[00:54.19]青春充满了不眠\n[00:57.71]是为了追寻更多的明天\n[01:13.32]\n[01:16.91]好似无尽的灯街\n[01:20.29]从不分你我照亮在心间\n[01:24.03]夜已承载心无眠\n[01:27.79]再巨大的伤悲皆已成灰\n[01:35.52]\n[01:45.98]如果你还没有睡\n[01:49.44]如果我还不停追\n[01:53.15]如果清醒是种罪\n[01:56.93]就把誓言带走 换承诺不回\n[02:01.31]如果你就是一切\n[02:04.35]如果我就是绝对\n[02:08.12]如果清醒是种罪\n[02:12.12]就让爱去蔓延 成全每个夜\n[02:18.38]\n[02:46.25]时过境迁的伤悲\n[02:49.46]搭配快乐的宣泄\n[02:53.19]如果清醒是种罪\n[02:56.83]你会不会怨怼 将就的明天\n[03:01.40]如果你就是一切\n[03:04.52]如果我就是绝对\n[03:08.29]如果清醒是种罪\n[03:11.97]就让爱去蔓延 成全每个夜\n[03:18.28]\n[03:31.45]记住激情的滋味\n[03:34.49]记住流泪的画面\n[03:38.23]如果清醒是种罪\n[03:41.98]就拿偏执的一切\n[03:44.50]放弃无聊的称谓\n[03:46.55]如果你真是一切\n[03:49.43]如同我真是绝对\n[03:53.09]如果夜留下暧昧\n[03:57.46]让你我不再挂念\n[03:59.42]最后成全每个谁\n[04:14.24]\n"
        lyc.split("\n").associate {

            (if (it.length > 8) {
                it.substring(1, 9)
            } else {
                it
            }) to if (it.length > 8) {
                it.substring(10)
            } else {
                ""
            }
        }
            .filter {
                it.key.isNotBlank()
            }
            .map {
                convertToSeconds(it.key) to it.value
            }.toList().onEach(::println)
    }

    Box(
        modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
            .border(.01.dp, color = Color(0x33ffffff), RoundedCornerShape(10.dp)).background(
                color = GlobalStyle.backgroundColor
            ).fillMaxWidth()


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
        val conRateAni by animateFloatAsState(if (isPlay) conRate else -40f, )
        var currentTime by remember { mutableStateOf(0f) }
        Column {
            Box {
                Bar(state, exitApplication, rightContent = {
                    RightBar {
                        androidx.compose.animation.AnimatedVisibility(
                            !show, enter =
                            fadeIn(), exit = fadeOut()
                        ) {
                            Row(
                                modifier = Modifier.offset { IntOffset(0, -5.dp.roundToPx()) }.width(400.dp)
                                    .fillMaxHeight()
                                    .padding(bottom = 6.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    "个性推荐",
                                    color = GlobalStyle.textCheckColor,
                                    fontSize = GlobalStyle.defaultFontSize

                                )
                                Text(
                                    "歌单",
                                    color = GlobalStyle.textUnCheckColor,
                                    fontSize = GlobalStyle.defaultFontSize
                                )
                                Text(
                                    "排行榜",
                                    color = GlobalStyle.textUnCheckColor,
                                    fontSize = GlobalStyle.defaultFontSize
                                )
                                Text(
                                    "歌手",
                                    color = GlobalStyle.textUnCheckColor,
                                    fontSize = GlobalStyle.defaultFontSize
                                )
                                Text(
                                    "最新音乐",
                                    color = GlobalStyle.textUnCheckColor,
                                    fontSize = GlobalStyle.defaultFontSize
                                )
                            }
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
                            Icon(
                                painterResource("image/ic_back.webp"),
                                contentDescription = null,
                                tint = Color(0xFFaeaeae),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                painterResource("image/ic_more.webp"),
                                contentDescription = null,
                                tint = Color(0xFFaeaeae),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                content()
                androidx.compose.animation.AnimatedVisibility(show, enter =
                slideIn { IntOffset(0, it.height) }, exit = slideOut { IntOffset(0, it.height) }) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(GlobalStyle.backgroundColor)) {
                        val width = with(LocalDensity.current) {
                            constraints.maxWidth.toDp()
                        }
                        val height = with(LocalDensity.current) {
                            constraints.maxHeight.toDp()
                        }
                        val ani = remember {
                            Animatable(0f, Float.VectorConverter)
                        }
                        LaunchedEffect(isPlay) {
                            if (isPlay) {
                                ani.animateTo(
                                    ani.value + 360f,
                                    animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing))
                                )
                            }
                        }
                        val lycIndex by remember {
                            derivedStateOf {
                                lycList.filter { currentTime >= it.first }
                                    .lastIndex
                            }
                        }
                        LazyColumn {
                            item {
                                Row(modifier = Modifier.size(width, height)) {
                                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                        Box(
                                            modifier = Modifier.fillMaxHeight().offset(y = (-50).dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(painterResource("image/ic_disk_around.webp"),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .shadow(10.dp, shape = CircleShape)
                                                    .graphicsLayer {
                                                        rotationZ = ani.value % 360f
                                                    }
                                                    .size(320.dp)


                                            )
                                            Image(painterResource("image/musicPic.jpg"),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .graphicsLayer {
                                                        rotationZ = ani.value % 360f
                                                        clip = true
                                                        shape = CircleShape
                                                    }

                                                    .size(212.dp)


                                            )
                                        }
                                        Box(
                                            modifier = Modifier.fillMaxHeight(),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Image(painterResource("image/ic_play_neddle.webp"),
                                                contentDescription = null,
                                                modifier = Modifier.size(160.dp)
                                                    .offset(x = 30.dp)
                                                    .graphicsLayer {
                                                        transformOrigin = TransformOrigin(0.280f, 0.12f)
                                                        rotationZ = conRateAni
                                                    }

                                            )

                                        }
                                    }
                                    val stateList = rememberLazyListState()
                                    val sa = rememberScrollbarAdapter(stateList)
                                    Box(
                                        modifier = Modifier.weight(1f).fillMaxHeight()
                                            .padding(vertical = 150.dp)
                                            .padding(end = 150.dp)
                                    ) {
                                        val brushTop = Brush.verticalGradient(
                                            listOf(
                                                globalStyle.current.backgroundColor,
                                                Color.Transparent,
                                            ),
                                            startY = 0f,
                                            endY = 80f

                                        )
                                        LazyColumn(
                                            modifier = Modifier.fillMaxSize()
                                                .drawWithCache {

                                                    onDrawWithContent {
                                                        drawContent()
                                                        drawRect(brushTop, size = Size(size.width, 80f))
                                                        rotate(180f) {
                                                            drawRect(brushTop, size = Size(size.width, 80f))
                                                        }


                                                    }
                                                }, state = stateList
                                        ) {
                                            item {
                                                Spacer(modifier = Modifier.height(80.dp))
                                            }
                                            items(lycList.size, key = {
                                                lycList[it].first
                                            }) {
                                                val lycColor by animateColorAsState(if (it == lycIndex) globalStyle.current.lycCheckColor else globalStyle.current.lycColor)
                                                val lycFontsize by animateFloatAsState(if (it == lycIndex) globalStyle.current.lycFontCheckSize else globalStyle.current.lycFontSize)

                                                Text(lycList[it].second, color = lycColor, fontSize = lycFontsize.sp)
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                            item {
                                                Spacer(modifier = Modifier.height(80.dp))
                                            }
                                        }
                                        VerticalScrollbar(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            adapter = sa,
                                            style = ScrollbarStyle(
                                                minimalHeight = 8.dp,
                                                thickness = 5.dp,
                                                shape = RoundedCornerShape(4.dp),
                                                hoverDurationMillis = 300,
                                                unhoverColor = globalStyle.current.scrollColor,
                                                hoverColor = globalStyle.current.scrollCheckColor
                                            )
                                        )
                                        LaunchedEffect(lycIndex) {
                                            if (!stateList.isScrollInProgress) {
                                                stateList.animateScrollToItem(max(0, lycIndex - 3))
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        //播放器
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            MediaPlayer(javaClass.getResource("/music/M500000SFLv10YFDuo.mp3")?.toURI().toString(), isPlayCallback = {
                isPlay = it
            }, processCallback = {
                process = it
            }, currentTimeChange = {
                currentTime = it
            }) { show = !show }
        }


    }

}





