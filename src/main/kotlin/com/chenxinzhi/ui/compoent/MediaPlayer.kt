package com.chenxinzhi.ui.compoent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenxinzhi.ui.style.globalStyle
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/9
 */
@Composable
fun MediaPlayer() {
    Box(
        modifier = Modifier.background(globalStyle.current.mediaPlayerBackgroundColor).fillMaxWidth()
            .height(62.dp)
    ) {
        MusicLinearProgressIndicator()
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
                            textAlign =TextAlign.Center
                        )
                        Box(Modifier.width(2.dp))
                        Text(
                            "-", fontSize = globalStyle.current.mediaPlayerMusicSingerNameSize,
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign =TextAlign.Center,
                            lineHeight = globalStyle.current.mediaPlayerMusicSingerNameSize
                        )
                        Box(Modifier.width(2.dp))

                        Text(
                            "告五人",
                            fontSize = globalStyle.current.mediaPlayerMusicSingerNameSize,
                            color = globalStyle.current.mediaPlayerMusicSingerNameColor,
                            textAlign =TextAlign.Center,
                            lineHeight = globalStyle.current.mediaPlayerMusicSingerNameSize
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun MusicLinearProgressIndicator() {
    BoxWithConstraints {
        val processWidth = constraints.maxWidth
        var process by remember { mutableStateOf(0f) }
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
        Canvas(
            Modifier
                .offset {
                    val fl = 12.dp.toPx() / 2 - 1
                    IntOffset(-fl.roundToInt() + processWidthUse.roundToInt(), -fl.roundToInt())
                }
                .draggable(rememberDraggableState { delta ->
                    process += delta / processWidth
                    println(process)

                }, Orientation.Horizontal)
                .size(12.dp)
                .hoverable(interactionSource).alpha(alpha)
        ) {
            drawCircle(mediaPlayerProcessColor)
        }

    }
}