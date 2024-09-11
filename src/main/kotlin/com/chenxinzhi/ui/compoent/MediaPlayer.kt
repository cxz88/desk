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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chenxinzhi.ui.style.globalStyle
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    Box(
        modifier = Modifier.background(globalStyle.current.mediaPlayerBackgroundColor).fillMaxWidth()
            .height(62.dp)
    ) {
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
                            fontSize = globalStyle.current.mediaPlayerMusicSingerNameSize,
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign = TextAlign.Center,
                            lineHeight = globalStyle.current.mediaPlayerMusicSingerNameSize
                        )
                    }

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