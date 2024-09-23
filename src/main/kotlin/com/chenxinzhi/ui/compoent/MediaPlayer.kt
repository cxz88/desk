package com.chenxinzhi.ui.compoent

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import com.chenxinzhi.api.Api
import com.chenxinzhi.sqlservice.FuncEnum
import com.chenxinzhi.sqlservice.getByKey
import com.chenxinzhi.sqlservice.updateByKey
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.utils.antialias
import com.chenxinzhi.utils.toComposePath
import com.chenxinzhi.viewmodel.media.MediaPlayerViewModel
import com.chenxinzhi.viewmodel.media.ProcessIndicatorViewModel
import com.github.panpf.sketch.AsyncImage
import com.sun.javafx.application.PlatformImpl
import javafx.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModel
import org.jetbrains.skia.IRect
import org.jetbrains.skia.Region
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/9
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPlayer(
    musicId: String = "",
    musicFlow: MutableStateFlow<String>,
    ref: MutableStateFlow<Long>,
    lycDeskShow: MutableStateFlow<Boolean>,
    mediaPlayerViewModel: MediaPlayerViewModel = viewModel {
        MediaPlayerViewModel()
    },
    isPlayCallback: (Boolean) -> Unit,
    processCallback: (Float) -> Unit,
    currentTimeChange: (Float) -> Unit,
    closeFlow: MutableStateFlow<Boolean>,
    showContent: () -> Unit,

    ) {
    val split = musicId.split(",")
    val url by remember(split) { mutableStateOf(split[0]) }
    val sss by ref.collectAsState()
    //更新播放索引
    LaunchedEffect(url, sss) {
        val s = getByKey(FuncEnum.PLAY_LIST, "").split(":%%19969685426854***")
        val s1 = getByKey(FuncEnum.MUSIC_ID, "")
        mediaPlayerViewModel.nowPlayIndex = if (s.size == 1 && s[0].isBlank()) {
            -1
        } else {
            val lastIndexOf = s.map { it.split(",")[0] }.lastIndexOf(s1.split(",")[0])
            lastIndexOf
        }
    }
    val pic by remember(split) { mutableStateOf(split[1]) }
    val name by remember(split) { mutableStateOf(split[2]) }
    val ysj by remember(split) { mutableStateOf(split[3]) }
    val rememberCoroutineScope = rememberCoroutineScope()
    remember(mediaPlayerViewModel.progress) {
        processCallback(mediaPlayerViewModel.progress)
    }
    var preTime by remember { mutableStateOf(0L) }
    remember(mediaPlayerViewModel.currentTime, mediaPlayerViewModel.isReady) {
        currentTimeChange(mediaPlayerViewModel.currentTime)
        //每隔5s插入一次
        if (System.currentTimeMillis() - preTime > 5000) {
            rememberCoroutineScope.launch {
                if (mediaPlayerViewModel.isReady) {
                    preTime = System.currentTimeMillis()
                    updateByKey(FuncEnum.PLAY_CURRENT_TIME, mediaPlayerViewModel.currentTime.toString())
                }
            }
        }

    }
    var count by remember { mutableStateOf(0) }
    DisposableEffect(musicId) {
        rememberCoroutineScope.launch {
            if (url.isBlank()) {
                mediaPlayerViewModel.isPause = true
                return@launch
            }
            Api.getMusicUrl(url)?.let {
                it.data.url.let { url ->
                    if (url.isNotBlank()) {
                        mediaPlayerViewModel.isPause = false
//                        if (count > 1) {
//                            mediaPlayerViewModel.currentTime = 0f
//                            mediaPlayerViewModel.isPause = false
//                        }
                        PlatformImpl.startup(
                            {
                                val media = javafx.scene.media.Media(url)
                                mediaPlayerViewModel.mediaPlayerState = javafx.scene.media.MediaPlayer(media).apply {
                                    volume = mediaPlayerViewModel.volume.toDouble()
                                    setOnReady {
                                        mediaPlayerViewModel.duration = media.duration.toSeconds().toFloat()
                                        rememberCoroutineScope.launch {
                                            if (count == 0) {
                                                val v = getByKey(FuncEnum.PLAY_CURRENT_TIME, "0").toFloat()
                                                mediaPlayerViewModel.currentTime = v
                                                mediaPlayerViewModel.mediaPlayerState?.seek(
                                                    Duration.seconds(
                                                        mediaPlayerViewModel.currentTime.toDouble()
                                                    )
                                                )
                                                val p = getByKey(FuncEnum.PLAY_OR_PAUSE_STATE, "0").toInt()
                                                mediaPlayerViewModel.isPause = p == 0
                                            }
                                            mediaPlayerViewModel.isReady = true
                                        }

                                    }
                                    currentTimeProperty().addListener { _, _, newValue ->
                                        if (mediaPlayerViewModel.isWait) {
                                            return@addListener
                                        }
                                        mediaPlayerViewModel.currentTime = newValue.toSeconds().toFloat()
                                    }
                                    setOnEndOfMedia {
                                        rememberCoroutineScope.launch {
                                            mediaPlayerViewModel.isPause = true
                                            val mode = getByKey(FuncEnum.PLAY_MODEL, "0").toInt()
                                            if (mode == 2) {
                                                mediaPlayerViewModel.isWait = true
                                                val d = 0.0
                                                mediaPlayerViewModel.currentTime = d.toFloat()
                                                mediaPlayerViewModel.mediaPlayerState?.seek(Duration.seconds(d))
                                                delay(1)
                                                mediaPlayerViewModel.isWait = false
                                                mediaPlayerViewModel.isPause = false
                                            } else {
                                                //如果设置了单曲循环,则快进到0,重新播放
                                                //如果有下一个则播放下一个
                                                val s = getByKey(FuncEnum.PLAY_LIST, "").split(":%%19969685426854***")
                                                if (s.isEmpty()) {
                                                    mediaPlayerViewModel.isWait = true
                                                    val d = 0.0
                                                    mediaPlayerViewModel.currentTime = d.toFloat()
                                                    mediaPlayerViewModel.mediaPlayerState?.seek(Duration.seconds(d))
                                                    delay(1)
                                                    mediaPlayerViewModel.isWait = false
                                                    mediaPlayerViewModel.isPause = false
                                                }
                                                if (mode == 0) {
                                                    musicFlow.value =
                                                        s[mediaPlayerViewModel.nowPlayIndex] + ",%%end1996888888888%%"
                                                    if (mediaPlayerViewModel.nowPlayIndex >= s.size - 1) {
                                                        mediaPlayerViewModel.nowPlayIndex = 0
                                                    }
                                                } else {
                                                    musicFlow.value =
                                                        s[Random.nextInt(0, s.size)] + ",%%end1996888888888%%"
                                                }

                                            }
                                        }


                                    }

                                    play()
                                }


                            }, false
                        )
                    }
                }
            }

        }

        onDispose {
            mediaPlayerViewModel.mediaPlayerState?.stop()
            mediaPlayerViewModel.mediaPlayerState?.dispose()
            mediaPlayerViewModel.mediaPlayerState = null
            count++
        }
    }
    Box(
        modifier = Modifier.background(globalStyle.current.mediaPlayerBackgroundColor).fillMaxWidth()
            .height(62.dp).pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes[0].isConsumed) {
                            continue;
                        }
                        mediaPlayerViewModel.showPaint = false
                        if (event.type == PointerEventType.Press) {
                            closeFlow.value = !closeFlow.value
                        }
                    }
                }
            }
    ) {
        val region = remember { Region() }
        MusicLinearProgressIndicator(mediaPlayerViewModel.progress) {
            //将当前的时间加上对应的百分比
            rememberCoroutineScope.launch {
                mediaPlayerViewModel.isWait = true
                val d = mediaPlayerViewModel.duration * it.toDouble()
                mediaPlayerViewModel.currentTime = d.toFloat()
                mediaPlayerViewModel.mediaPlayerState?.seek(Duration.seconds(d))
                delay(1)
                mediaPlayerViewModel.isWait = false
            }

        }
        remember(mediaPlayerViewModel.isPause, mediaPlayerViewModel.isReady) {
            if (mediaPlayerViewModel.isPause) {
                mediaPlayerViewModel.mediaPlayerState?.pause()
            } else {
                mediaPlayerViewModel.mediaPlayerState?.play()
            }
            isPlayCallback(!mediaPlayerViewModel.isPause)
            rememberCoroutineScope.launch {
                if (mediaPlayerViewModel.isReady) {
                    updateByKey(FuncEnum.PLAY_OR_PAUSE_STATE, if (mediaPlayerViewModel.isPause) "0" else "1")
                }
            }
        }
        val paint = remember {
            Paint().apply {
                isAntiAlias = true
            }
        }
        val musicControlColor = globalStyle.current.musicControlColor
        Box(modifier = Modifier.padding(top = 2.dp)) {
            Row(modifier = Modifier.width(300.dp)) {
                val s = with(LocalDensity.current) {
                    42.dp.toPx().roundToInt()
                }
                AsyncImage(
                    pic.replace("/1000/", "/$s/"),
                    contentDescription = null,
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 10.dp)
                        .size(42.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onClick {
                            showContent()
                        },
                    filterQuality = FilterQuality.High
                )
                Box(modifier = Modifier.width(10.dp))
                Column {
                    Box(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(20.dp)) {
                        Text(
                            name.let {
                                it.ifBlank {
                                    "暂无歌曲"
                                }
                            },
                            overflow = TextOverflow.Ellipsis,
                            fontSize = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicNameSize.value.dp.toSp()
                            },
                            lineHeight = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicNameSize.value.dp.toSp()
                            },
                            maxLines = 1,
                            color = globalStyle.current.mediaPlayerMusicNameColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.wrapContentHeight().height(with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicNameSize.toDp() + 2.33.dp
                            }).offset(y = (-2.33).dp)

                        )
                        Box(Modifier.width(2.dp))
                        Text(
                            "-", fontSize = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.value.dp.toSp()
                            },
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            lineHeight = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.value.dp.toSp()
                            },
                            modifier = Modifier.wrapContentHeight().height(with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.toDp() + 1.55.dp
                            }).offset(y = (-1.55).dp)
                        )
                        Box(Modifier.width(2.dp))

                        Text(
                            ysj.let {
                                it.ifBlank {
                                    "暂无歌手"
                                }
                            },

                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.value.dp.toSp()
                            },
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign = TextAlign.Center,
                            lineHeight = with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.value.dp.toSp()
                            },
                            modifier = Modifier.wrapContentHeight().height(with(LocalDensity.current) {
                                globalStyle.current.mediaPlayerMusicSingerNameSize.toDp() + 1.55.dp
                            }).offset(y = (-1.55).dp)
                        )
                    }
                    Box(modifier = Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            mediaPlayerViewModel.currentTime.toInt().let {
                                "${(it / 60).toString().padStart(2, '0')}:${(it % 60).toString().padStart(2, '0')}"
                            },
                            overflow = TextOverflow.Ellipsis,
                            fontSize = globalStyle.current.durationFontSize,
                            lineHeight = globalStyle.current.durationFontSize,
                            color = globalStyle.current.durationColor,
                            textAlign = TextAlign.Center
                        )
                        Box(Modifier.width(3.dp))
                        Text(
                            "/", fontSize = globalStyle.current.durationFontSize,
                            color = globalStyle.current.durationColor,
                            textAlign = TextAlign.Center,
                            lineHeight = globalStyle.current.durationFontSize
                        )
                        Box(Modifier.width(3.dp))

                        Text(
                            mediaPlayerViewModel.duration.roundToInt().let {
                                "${(it / 60).toString().padStart(2, '0')}:${(it % 60).toString().padStart(2, '0')}"
                            },
                            fontSize = globalStyle.current.durationFontSize,
                            color = globalStyle.current.durationColor,
                            textAlign = TextAlign.Center,
                            lineHeight = globalStyle.current.durationFontSize
                        )
                    }


                }
            }
            //播放按钮
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Canvas(Modifier.size(20.dp).pointerHoverIcon(PointerIcon.Hand).pointerInput(Unit) {
                    detectTapGestures {
                        //上一首
                        rememberCoroutineScope.launch {
                            val s = getByKey(FuncEnum.PLAY_LIST, "").split(":%%19969685426854***")
                            val index = mediaPlayerViewModel.nowPlayIndex - 1
                            mediaPlayerViewModel.nowPlayIndex = if (index < 0) {
                                s.size - 1
                            } else {
                                index
                            }
                            musicFlow.value = s[mediaPlayerViewModel.nowPlayIndex] + ",%%end1996888888888%%"
                        }
                    }
                }) {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 3,
                        radius = size.minDimension / 2f,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        //每个三角形的角的内圆形弧度太大会导致
                        rounding = CornerRounding(
                            size.minDimension / 10,
                            smoothing = 0.1f
                        )
                    )
                    val roundedPolygonPath = roundedPolygon.toComposePath()
                    rotate(180f) {
                        drawPath(roundedPolygonPath, musicControlColor)
                    }
                    drawLine(
                        musicControlColor,
                        start = Offset(0f, 4f),
                        end = Offset(0f, size.height - 4f),
                        cap = StrokeCap.Round,
                        strokeWidth = 2f
                    )

                }
                Box(modifier = Modifier.width(15.dp))
                Box(modifier = Modifier
                    .composed {
                        if (mediaPlayerViewModel.showPaint) {
                            Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                        } else {
                            Modifier
                        }
                    }
                    .size(41.dp)
                    .drawWithCache {
                        val path = Path()
                        path.lineTo(0f, 50f)
                        path.addOval(Rect(Offset.Zero, size))
                        region.setPath(path.asSkiaPath(), Region().apply {
                            setRect(IRect.makeLTRB(0, 0, size.width.roundToInt(), size.height.roundToInt()))
                        }
                        )
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 3,
                            radius = size.minDimension / 4.1f,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            //每个三角形的角的内圆形弧度太大会导致
                            rounding = CornerRounding(
                                size.minDimension / 30,
                                smoothing = 0.1f
                            )

                        )
                        val roundedPolygonPath = roundedPolygon.toComposePath()
                        onDrawBehind {

                            drawIntoCanvas {
                                paint.color = musicControlColor
                                it.drawCircle(
                                    Offset(size.width / 2, size.height / 2),
                                    radius = size.minDimension / 2f,
                                    paint = paint
                                )
                                if (mediaPlayerViewModel.isPause) {
                                    paint.color = Color.White
                                    it.drawPath(roundedPolygonPath, paint = paint)
                                } else {
                                    drawLine(
                                        Color.White,
                                        Offset(17.5.dp.toPx(), 13.5.dp.toPx()),
                                        Offset(17.5.dp.toPx(), size.height - 13.5.dp.toPx()),
                                        cap = StrokeCap.Round,
                                        strokeWidth = 2.8.dp.toPx()
                                    )
                                    drawLine(
                                        Color.White,
                                        Offset(size.width - 17.5.dp.toPx(), 13.5.dp.toPx()),
                                        Offset(size.width - 17.5.dp.toPx(), size.height - 13.5.dp.toPx()),
                                        cap = StrokeCap.Round,
                                        strokeWidth = 2.8.dp.toPx()
                                    )
                                }

                            }


                        }


                    }.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pointerInputChange = event.changes[0]
                                pointerInputChange.consume()
                                val contains = region.contains(
                                    pointerInputChange.position.x.roundToInt(),
                                    pointerInputChange.position.y.roundToInt()
                                )
                                mediaPlayerViewModel.showPaint = contains
                                if (!contains) {
                                    continue
                                }
                                if (event.type != PointerEventType.Press) {
                                    continue
                                }
                                var b = true
                                while (b) {
                                    val e = awaitPointerEvent()
                                    e.changes.forEach {
                                        it.consume()
                                    }
                                    if (e.type == PointerEventType.Move) {
                                        //判断是否超过范围
                                        e.changes.forEach {

                                            if (!region.contains(
                                                    it.position.x.roundToInt(),
                                                    it.position.y.roundToInt()
                                                )
                                            ) {
                                                b = false
                                                return@forEach
                                            }
                                        }
                                    } else if (e.type == PointerEventType.Release) {
                                        //执行回掉并退出
                                        mediaPlayerViewModel.isPause = !mediaPlayerViewModel.isPause
                                        b = false
                                    }
                                }
                            }
                        }
                    }
                )
                Box(modifier = Modifier.width(15.dp))
                Canvas(Modifier.size(20.dp).pointerHoverIcon(PointerIcon.Hand).pointerInput(Unit) {
                    detectTapGestures {
                        rememberCoroutineScope.launch {
                            val s = getByKey(FuncEnum.PLAY_LIST, "").split(":%%19969685426854***")
                            val index = mediaPlayerViewModel.nowPlayIndex + 1
                            mediaPlayerViewModel.nowPlayIndex = if (index > s.size - 1) {
                                0
                            } else {
                                index
                            }
                            musicFlow.value = s[mediaPlayerViewModel.nowPlayIndex] + ",%%end1996888888888%%"
                        }
                    }
                }) {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 3,
                        radius = size.minDimension / 2f,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        //每个三角形的角的内圆形弧度太大会导致
                        rounding = CornerRounding(
                            size.minDimension / 10,
                            smoothing = 0.1f
                        )
                    )
                    val roundedPolygonPath = roundedPolygon.toComposePath()

                    drawPath(roundedPolygonPath, musicControlColor)

                    drawLine(
                        musicControlColor,
                        start = Offset(size.width, 4f),
                        end = Offset(size.width, size.height - 4f),
                        cap = StrokeCap.Round,
                        strokeWidth = 2f
                    )

                }

            }

            //循环按钮
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxSize().padding(end = 10.dp)
            ) {

                Column(
                    modifier = Modifier.width(60.dp).offset(x = (60).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //动画设置显示隐藏
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val hover by interactionSource.collectIsHoveredAsState()
                    val alpha by animateFloatAsState(
                        if (hover) {
                            1f
                        } else {
                            0f
                        }
                    )
                    Text(
                        "播放列表",
                        fontSize = with(LocalDensity.current) {
                            12.dp.toSp()
                        },
                        color = globalStyle.current.RightControlColor,
                        textAlign = TextAlign.Center,
                        lineHeight = with(LocalDensity.current) {
                            12.dp.toSp()
                        },
                        modifier = Modifier
                            .alpha(alpha)
                            .shadow(5.dp, spotColor = Color.White, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(globalStyle.current.RightControlBackgroundColor)
                            .padding(2.dp)
                    )
                    Icon(
                        painterResource("image/ic_play_list.webp"),
                        tint = globalStyle.current.RightControlColor,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                            .hoverable(interactionSource)
                            .antialias()
                    )
                }

                Column(
                    modifier = Modifier.width(60.dp).offset(x = (30).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //动画设置显示隐藏
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val hover by interactionSource.collectIsHoveredAsState()
                    val alpha by animateFloatAsState(
                        if (hover) {
                            1f
                        } else {
                            0f
                        }
                    )
                    Text(
                        mediaPlayerViewModel.playModeList[mediaPlayerViewModel.nowPlayerModel].second,
                        fontSize = with(LocalDensity.current) {
                            12.dp.toSp()
                        },
                        color = globalStyle.current.RightControlColor,
                        textAlign = TextAlign.Center,
                        lineHeight = with(LocalDensity.current) {
                            12.dp.toSp()
                        },
                        modifier = Modifier
                            .alpha(alpha)
                            .shadow(5.dp, spotColor = Color.White, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(globalStyle.current.RightControlBackgroundColor)
                            .padding(2.dp)
                    )
                    Icon(
                        painterResource(mediaPlayerViewModel.playModeList[mediaPlayerViewModel.nowPlayerModel].first),
                        tint = globalStyle.current.RightControlColor,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                            .hoverable(interactionSource)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    mediaPlayerViewModel.setNowPlayerModel(mediaPlayerViewModel.nowPlayerModel + 1)
                                }
                            }.antialias()
                    )
                }
                //歌词
                Column(
                    Modifier.width(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //动画设置显示隐藏
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    remember(mediaPlayerViewModel.showDeskLyc) {
                        lycDeskShow.value = mediaPlayerViewModel.showDeskLyc
                    }
                    val show by lycDeskShow.collectAsState()
                    remember(show) {
                        mediaPlayerViewModel.setShowDeskLyc(show)
                    }
                    val c by animateColorAsState(if (mediaPlayerViewModel.showDeskLyc) globalStyle.current.RightControlUseColor else globalStyle.current.RightControlColor)
                    val hover by interactionSource.collectIsHoveredAsState()
                    val alpha by animateFloatAsState(
                        if (hover) {
                            1f
                        } else {
                            0f
                        }
                    )
                    Text(
                        "${if (mediaPlayerViewModel.showDeskLyc) "关闭" else "打开"}歌词",
                        fontSize = 12.sp,
                        color = globalStyle.current.RightControlColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp,
                        modifier = Modifier
                            .alpha(alpha)
                            .shadow(5.dp, spotColor = Color.White, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(globalStyle.current.RightControlBackgroundColor)
                            .padding(2.dp)
                    )
                    Box(modifier = Modifier.height(20.dp).offset(y = .5.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "词", fontSize = with(LocalDensity.current) {
                                17.dp.toSp()
                            }, lineHeight = with(LocalDensity.current) {
                                20.dp.toSp()
                            }, modifier = Modifier
                                .size(20.dp)
                                .hoverable(interactionSource)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        mediaPlayerViewModel.setShowDeskLyc(!mediaPlayerViewModel.showDeskLyc)

                                    }
                                }.antialias(), color = c, textAlign = TextAlign.Center
                        )
                    }
                }
                //音量
                Column(
//                    Modifier.offset(y = -62.dp).width(20.dp).fillMaxHeight().background(Color(0xff363636)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(x = (-15).dp).width(26.dp)
                        .fillMaxHeight().pointerInput(Unit) {
                            detectTapGestures { }
                        }
                ) {
                    var show by remember { mutableStateOf(false) }
                    LaunchedEffect(closeFlow.collectAsState().value) {
                        show = false
                    }

                    Box(modifier = Modifier.height(12.dp))
                    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.height(8.dp)) {
                        androidx.compose.animation.AnimatedVisibility(
                            show, enter = fadeIn(tween(50)), exit = fadeOut(
                                tween(50)
                            ), modifier = Modifier.width(26.dp)
                        ) {
                            Box(contentAlignment = Alignment.TopCenter) {
                                Canvas(modifier = Modifier.width(26.dp)) {
                                    val path = Path().apply {
                                        // 定义一个带有圆角的矩形
                                        addRoundRect(
                                            roundRect = RoundRect(
                                                0f,
                                                -100.dp.toPx(),
                                                size.width,
                                                0f,
                                                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                                            )
                                        )
                                    }
                                    // 填充矩形
                                    drawPath(
                                        path = path,
                                        color = Color(0xff363636),
                                        style = Fill
                                    )
                                    //再画一个小矩形,用来显示进度
                                    //填充三角形
                                    val pathTriangle = Path().apply {
                                        moveTo(8.dp.toPx(), 0f)
                                        lineTo(size.width - (8.dp.toPx()), 0f)
                                        lineTo(size.width / 2, size.width - (21.dp.toPx()))
                                        close()
                                    }
                                    drawPath(
                                        path = pathTriangle,
                                        color = Color(0xff363636),
                                        style = Fill
                                    )
                                    drawRoundRect(
                                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                                        color = Color(0xffd33a31),
                                        topLeft = Offset(size.width / 2 - (2.dp.toPx()), -85.dp.toPx()),
                                        size = Size(4.dp.toPx(), 75.dp.toPx()),
                                    )
                                }
                                val m = with(LocalDensity.current) {
                                    -14.dp.toPx()
                                }
                                val mx = with(LocalDensity.current) {
                                    -89.dp.toPx()
                                }
                                var yO by remember { mutableStateOf(m - (abs(mx) - abs(m)) * mediaPlayerViewModel.volume) }
                                val d = rememberDraggableState {
                                    yO = kotlin.math.max(min(yO + it, m), mx)

                                }
                                remember(yO) {
                                    mediaPlayerViewModel.volume = (abs(yO) - abs(m)) / (abs(mx) - abs(m))
                                    mediaPlayerViewModel.mediaPlayerState?.volume =
                                        mediaPlayerViewModel.volume.toDouble()

                                }
                                //可以拖动的圆
                                Box(
                                    modifier = Modifier
                                        .offset {
                                            IntOffset(0, yO.roundToInt())
                                        }
                                        .clip(CircleShape).background(Color(0xffd33a31)).size(8.dp)
                                        .draggable(d, Orientation.Vertical)
                                )
                            }
                        }


                    }
                    Icon(
                        painterResource("image/ic_volumn.webp"),
                        contentDescription = null,
                        tint = globalStyle.current.RightControlColor,
                        modifier = Modifier.size(20.dp)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    show = !show
                                }
                            }.antialias()
                    )
                }


            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicLinearProgressIndicator(
    processOut: Float,
    processIndicatorViewModel: ProcessIndicatorViewModel = viewModel { ProcessIndicatorViewModel() },
    seek: (Float) -> Unit
) {
    val process by animateFloatAsState(
        if (processIndicatorViewModel.useInner) processIndicatorViewModel.processInner else processOut,
        tween(1)
    )
    BoxWithConstraints {
        val processWidth = constraints.maxWidth
        val processWidthUse = min(processWidth * process, processWidth.toFloat())
        val mediaPlayerProcessColor = globalStyle.current.mediaPlayerProcessColor
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(2.dp)

        ) {
            drawRect(mediaPlayerProcessColor, size = Size(processWidthUse, size.height))
        }
        val hover by processIndicatorViewModel.interactionSource.collectIsHoveredAsState()
        val alpha by animateFloatAsState(if (hover) 1f else 0f)
        Box(modifier = Modifier.fillMaxWidth().height(12.dp).hoverable(processIndicatorViewModel.interactionSource)
            .pointerInput(Unit) {
                detectTapGestures {
                    seek(it.x / processWidth)
                }
            }
        ) {
            Canvas(
                Modifier
                    .offset {
                        val fl = 12.dp.toPx() / 2 - 1
                        IntOffset(-fl.roundToInt() + processWidthUse.roundToInt(), -fl.roundToInt())
                    }
                    .size(12.dp)
                    .alpha(alpha)
                    .pointerInput(Unit) {
                        detectDragGestures(matcher = PointerMatcher.Primary, onDragEnd = {
                            //应用进度条更改
                            seek(processIndicatorViewModel.processInner)
                            processIndicatorViewModel.useInner = false

                        }, onDragStart = {
                            processIndicatorViewModel.processInner = process

                        }) {
                            processIndicatorViewModel.useInner = true
                            processIndicatorViewModel.processInner += (it.x / processWidth)
                        }

                    }
            ) {
                drawCircle(mediaPlayerProcessColor)
            }
        }


    }
}