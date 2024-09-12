package com.chenxinzhi.ui.compoent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.utils.toComposePath
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.IRect
import org.jetbrains.skia.Region
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/9
 */
@Composable
fun MediaPlayer(url: String = "") {
    var duration by remember { mutableStateOf(0f) }
    var isLoaded by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(1f) }
    var mediaPlayerState by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentTime by remember { mutableStateOf(0f) }
    val progress by remember {
        derivedStateOf {
            if (currentTime == 0f && duration == 0f) {
                0f
            } else {
                currentTime / duration
            }
        }
    }
    val rememberCoroutineScope = rememberCoroutineScope()
    var isWait by remember { mutableStateOf(false) }
    DisposableEffect(url) {
        Platform.startup {
            val media = Media(url)
            val mediaPlayer = MediaPlayer(media).apply {
                setOnReady {
                    duration = media.duration.toSeconds().toFloat()
                    isLoaded = true
                }
                currentTimeProperty().addListener { _, _, newValue ->
                    if (isWait) {
                        return@addListener
                    }
                    currentTime = newValue.toSeconds().toFloat()


                }
                setOnEndOfMedia {
                    isPlaying = false
                }

                volumeProperty().value = volume.toDouble()
            }
            mediaPlayerState = mediaPlayer.apply {
                cycleCount = MediaPlayer.INDEFINITE
                play()
            }

        }

        onDispose {
            mediaPlayerState?.dispose()
        }
    }
    var showPaint by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.background(globalStyle.current.mediaPlayerBackgroundColor).fillMaxWidth()
            .height(62.dp).pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes[0].isConsumed) {
                            continue;
                        }
                        showPaint = false
                    }
                }
            }
    ) {
        val region = remember { Region() }
        var isPlay by remember { mutableStateOf(false) }
        MusicLinearProgressIndicator(progress) {
            //将当前的时间加上对应的百分比
            rememberCoroutineScope.launch {
                isWait = true
                val d = duration * it.toDouble()
                currentTime = d.toFloat()
                mediaPlayerState?.seek(Duration.seconds(d))
                delay(1)
                isWait = false
            }

        }
        if (isPlay) {
            mediaPlayerState?.pause()
        } else {
            mediaPlayerState?.play()
        }
        val paint = remember {
            Paint().apply {
                isAntiAlias = true
            }
        }
        val musicControlColor = globalStyle.current.musicControlColor
        Box(modifier = Modifier.padding(top = 2.dp)) {
            Row {
                Image(
                    painterResource("image/musicPic.jpg"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 10.dp)
                        .size(42.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Box(modifier = Modifier.width(10.dp))
                Column {
                    Box(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "带我去找夜生活",
                            overflow = TextOverflow.Ellipsis,
                            fontSize = globalStyle.current.mediaPlayerMusicNameSize,
                            lineHeight = globalStyle.current.mediaPlayerMusicNameSize,
                            color = globalStyle.current.mediaPlayerMusicNameColor,
                            textAlign = TextAlign.Center
                        )
                        Box(Modifier.width(2.dp))
                        Text(
                            "-", fontSize = globalStyle.current.mediaPlayerMusicSingerNameSize,
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign = TextAlign.Center,
                            lineHeight = globalStyle.current.mediaPlayerMusicSingerNameSize
                        )
                        Box(Modifier.width(2.dp))

                        Text(
                            "告五人",
                            overflow = TextOverflow.Ellipsis,
                            fontSize = globalStyle.current.mediaPlayerMusicSingerNameSize,
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign = TextAlign.Center,
                            lineHeight = globalStyle.current.mediaPlayerMusicSingerNameSize
                        )
                    }
                    Box(modifier = Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            currentTime.toInt().let {
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
                            duration.roundToInt().let {
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
                Canvas(Modifier.size(20.dp).pointerHoverIcon(PointerIcon.Hand)){
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
                    rotate(180f){
                        drawPath(roundedPolygonPath, musicControlColor)
                    }
                    drawLine(musicControlColor, start = Offset(0f,4f), end = Offset(0f,size.height-4f), cap = StrokeCap.Round, strokeWidth = 2f)

                }
                Box(modifier = Modifier.width(15.dp))
                Box(modifier = Modifier
                    .composed {
                        if (showPaint) {
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
                                if (isPlay) {
                                    paint.color = Color.White
                                    it.drawPath(roundedPolygonPath, paint = paint)
                                } else {
                                    drawLine(
                                        Color.White,
                                        Offset(17.5f, 13.5f),
                                        Offset(17.5f, size.height - 13.5f),
                                        cap = StrokeCap.Round,
                                        strokeWidth = 2.8f
                                    )
                                    drawLine(
                                        Color.White,
                                        Offset(size.width - 17.5f, 13.5f),
                                        Offset(size.width - 17.5f, size.height - 13.5f),
                                        cap = StrokeCap.Round,
                                        strokeWidth = 2.8f
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
                                showPaint = contains
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
                                        isPlay = !isPlay
                                        b = false
                                    }
                                }
                            }
                        }
                    }
                )
                Box(modifier = Modifier.width(15.dp))
                Canvas(Modifier.size(20.dp).pointerHoverIcon(PointerIcon.Hand)){
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

                    drawLine(musicControlColor, start = Offset(size.width,4f), end = Offset(size.width,size.height-4f), cap = StrokeCap.Round, strokeWidth = 2f)

                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicLinearProgressIndicator(processOut: Float, seek: (Float) -> Unit) {
    var processInner by remember { mutableStateOf(0f) }
    var useInner by remember { mutableStateOf(false) }
    val process by animateFloatAsState(if (useInner) processInner else processOut, tween(1))
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
        val interactionSource = remember { MutableInteractionSource() }
        val hover by interactionSource.collectIsHoveredAsState()
        val alpha by animateFloatAsState(if (hover) 1f else 0f)
        Box(modifier = Modifier.fillMaxWidth().height(12.dp).hoverable(interactionSource)
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
                            seek(processInner)
                            useInner = false

                        }, onDragStart = {
                            processInner = process

                        }) {
                            useInner = true
                            processInner += (it.x / processWidth)
                        }

                    }
            ) {
                drawCircle(mediaPlayerProcessColor)
            }
        }


    }
}