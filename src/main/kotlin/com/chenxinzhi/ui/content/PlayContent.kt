package com.chenxinzhi.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.FrameWindowScope
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.viewmodel.content.PlayContentViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.viewmodel.viewModel
import kotlin.math.max

@Composable
fun FrameWindowScope.PlayContent(
    //原来的右边的内容
    content: @Composable (FrameWindowScope.() -> Unit),
    show: Boolean,
    isPlay: Boolean,
    currentTime: Float,
    conRateAni: Float,
    lycContent: MutableStateFlow<String>,
    searchList: MutableStateFlow<List<String>>,
    searchTipShow: MutableStateFlow<Boolean>,
    playContentViewModel: PlayContentViewModel = viewModel {
        PlayContentViewModel()
    },

    ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        content()
        AnimatedVisibility(
            show, enter =
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
                val lycIndex by remember(currentTime) {
                    derivedStateOf {
                        max(0, playContentViewModel.lycList.filter { currentTime >= it.first }
                            .lastIndex)

                    }
                }
                remember(lycIndex) {
                    lycContent.value = playContentViewModel.lycList[lycIndex].second
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
                                    items(playContentViewModel.lycList.size, key = {
                                        playContentViewModel.lycList[it].first
                                    }) {
                                        val lycColor by animateColorAsState(if (it == lycIndex) globalStyle.current.lycCheckColor else globalStyle.current.lycColor)
                                        val lycFontsize by animateFloatAsState(if (it == lycIndex) globalStyle.current.lycFontCheckSize else globalStyle.current.lycFontSize)

                                        Text(
                                            playContentViewModel.lycList[it].second,
                                            color = lycColor,
                                            fontSize = lycFontsize.sp,
                                            overflow = TextOverflow.Ellipsis, maxLines = 1
                                        )
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
        //搜索框
        val showTip by searchTipShow.collectAsState()
        val ap by animateFloatAsState(if (showTip) 1f else 0f)
        Box(
            modifier = Modifier.graphicsLayer {
                alpha = ap
            }.clip(RoundedCornerShape(8.dp)).height(300.dp).width(350.dp)
                .background(Color(0xff363636))
        ) {

            val searTip by searchList.collectAsState()
            Column(modifier = Modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.Center) {
                searTip.filter { it.isNotBlank() }.forEachIndexed { index, item ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val collectIsHoveredAsState by interactionSource.collectIsHoveredAsState()
                    val bg by animateColorAsState(
                        if (collectIsHoveredAsState) {
                            Color(0xff333333)
                        } else {
                            Color.Transparent
                        }
                    , tween(1, easing = LinearEasing)
                    )
                    BasicText(
                        item,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None),
                            lineHeight = with(LocalDensity.current){
                                24.dp.toSp()
                            },
                        ),
                        modifier = Modifier.height(28.dp).fillMaxWidth().background(bg).pointerHoverIcon(PointerIcon.Hand)
                            .hoverable(interactionSource)
                            .padding(horizontal = 10.dp)
                        ,
                        overflow = TextOverflow.Ellipsis,
                        color = {
                            Color(0xffb9b9b9)
                        },
                    )
                }
            }
        }
    }
}