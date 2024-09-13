package com.chenxinzhi.utils

import androidx.compose.ui.graphics.Path
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon

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