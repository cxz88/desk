package com.chenxinzhi.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import java.awt.Component
import java.awt.event.MouseEvent

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/5
 */

fun convertToSeconds(time: String): Double {
    val parts = time.split(":")
    val minutes = parts[0].toInt()
    val secondsAndMillis = parts[1].split(".")
    val seconds = secondsAndMillis[0].toInt()
    val millis = secondsAndMillis[1].toInt()

    // 将分钟转换为秒，并加上秒和毫秒（毫秒转换为秒）
    return minutes * 60 + seconds + millis / 1000.0
}

fun RoundedPolygon.toComposePath() =
    Path().apply { pathFromCubic(this, cubics) }


// Code from androidx.graphics.shapes.RoundedPolygon.toPath, which is only available for the Android
// target -- and returns an Android Path rather than a Compose Path.

private fun pathFromCubic(path: Path, cubics: List<Cubic>) {
    var first = true
    path.rewind()
    for (element in cubics) {
        if (first) {
            path.moveTo(element.anchor0X, element.anchor0Y)
            first = false
        }
        path.cubicTo(
            element.control0X,
            element.control0Y,
            element.control1X,
            element.control1Y,
            element.anchor1X,
            element.anchor1Y
        )
    }
    path.close()
}


fun Modifier.addMove(state: WindowState) =
    pointerInput(Unit) {
        awaitPointerEventScope {
            var preXOnScreen = 0
            var preYOnScreen = 0
            var startX = 0
            var startY = 0
            var posX = 0.dp
            var posY = 0.dp
            while (true) {
                val awaitPointerEvent = awaitPointerEvent()
                if (awaitPointerEvent.changes[0].isConsumed) {
                    continue
                }
                if (awaitPointerEvent.type == PointerEventType.Press) {
                    val ne = awaitPointerEvent.nativeEvent
                    if (ne is MouseEvent) {
                        preXOnScreen = ne.xOnScreen
                        preYOnScreen = ne.yOnScreen
                        val component = ne.source as Component
                        startX = component.x
                        startY = component.y
                        val position = state.position
                        posX = position.x
                        posY = position.y
                    }
                } else if (awaitPointerEvent.type == PointerEventType.Move) {
                    val nativeEvent = awaitPointerEvent.nativeEvent
                    if (nativeEvent is MouseEvent) {
                        if (nativeEvent.modifiersEx != MouseEvent.BUTTON1_DOWN_MASK) {
                            continue
                        }
                        val xOnScreen = nativeEvent.xOnScreen
                        val yOnScreen = nativeEvent.yOnScreen
                        val xOffset = xOnScreen - preXOnScreen
                        val yOffset = yOnScreen - preYOnScreen
                        state.position =
                            WindowPosition(
                                posX + xOffset.sp.toDp() + startX.sp.toDp(),
                                posY + yOffset.sp.toDp() + startY.sp.toDp()
                            )
                    }


                }
            }
        }


    }

fun Modifier.antialias() = graphicsLayer {
    // 开启抗锯齿
    renderEffect= BlurEffect(0f,0f, TileMode.Decal)
}