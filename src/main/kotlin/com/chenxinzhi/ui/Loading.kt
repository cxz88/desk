package com.chenxinzhi.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenxinzhi.model.LoadingViewModel
import com.chenxinzhi.ui.style.globalStyle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    viewModel: LoadingViewModel = moe.tlaster.precompose.viewmodel.viewModel { LoadingViewModel() },

    ) {
    LaunchedEffect(Unit) {
        viewModel.start()
    }
    val count = 8
    val rotateAngle = (360 / count).toDouble()
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        //1284总宽度  菊花宽度：209    17宽 38长     106     17/53  38/53
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.50f)
                        .aspectRatio(1f)
                ) {
                    val r = size.width / 2
                    //圆弧形的矩形 长度
                    val drawWidth = 0.50 * r
                    //圆弧形的矩形 宽度
                    val strokeWidth = 0.32 * r

                    for (index in 1..count) {
                        val startX =
                            (r + (r - drawWidth) * cos(Math.toRadians(rotateAngle * index))).toFloat()
                        val startY =
                            (r - (r - drawWidth) * sin(Math.toRadians(rotateAngle * index))).toFloat()
                        val endX = (r + r * cos(Math.toRadians(rotateAngle * index))).toFloat()
                        val endY = (r - r * sin(Math.toRadians(rotateAngle * index))).toFloat()
                        drawLine(
                            color = viewModel.mColor[index - 1],
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            cap = StrokeCap.Round,
                            strokeWidth = strokeWidth.toFloat(),
                        )
                    }

                }
            }
            Text("正在玩命加载中...", fontSize = 15.sp, color = globalStyle.current.lycCheckColor)
        }
    }
}
