package com.chenxinzhi.utils

import androidx.compose.foundation.shape.GenericShape

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/29
 */
val AXin =  GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    moveTo(width / 2, height * 0.3f)
    cubicTo(width * 0.2f, 0f, 0f, height * 0.4f, width / 2, height)
    cubicTo(width, height * 0.4f, width * 0.8f, 0f, width / 2, height * 0.3f)
}