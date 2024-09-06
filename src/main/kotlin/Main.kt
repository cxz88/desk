
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.chenxinzhi.ui.AppBar
import com.chenxinzhi.ui.GlobalStyle
import com.chenxinzhi.ui.Swipe
import com.chenxinzhi.ui.globalStyle
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val state = rememberWindowState(size = DpSize(1000.dp, 700.dp))
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        state = state,
        transparent = true,
        title = "",
        resizable = false,
    ) {
        CompositionLocalProvider(globalStyle provides GlobalStyle) {
            MaterialTheme {
                Column {
                    AppBar(state = state, { exitApplication() }) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .width(200.dp).fillMaxHeight().background(GlobalStyle.rightColor)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painterResource("image/avatar.jpeg"),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(60.dp)
                                                .padding(10.dp)
                                                .clip(CircleShape)
                                        )
                                        Text(
                                            "往事知多少",
                                            fontSize = globalStyle.current.avatarFontSize,
                                            color = globalStyle.current.avatarFontColor
                                        )
                                        Box(modifier = Modifier.width(8.dp))
                                        Icon(painter = painterResource("image/ic_triangle_right.webp"),
                                            contentDescription = null,
                                            tint = globalStyle.current.avatarFontRightIconColor,
                                            modifier = Modifier
                                                .offset { IntOffset(0, 2.dp.roundToPx()) }
                                                .size(8.dp)

                                        )
                                    }
                                    val listItem = listOf(
                                        "发现音乐" to painterResource("image/ic_sound_effect.webp"),
                                        "播客" to painterResource("image/ic_sound_effect.webp"),
                                        "私人漫游" to painterResource("image/ic_sound_effect.webp"),
                                        "视频" to painterResource("image/ic_sound_effect.webp"),
                                        "关注" to painterResource("image/ic_sound_effect.webp"),
                                    )
                                    Box {
                                        val scrollState = rememberLazyListState()
                                        LazyColumn(state = scrollState) {

                                            val checkItem = 0
                                            itemsIndexed(listItem) { index, item ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth().padding(end = 10.dp)
                                                        .height(35.dp)
                                                        .background(
                                                            if (index == checkItem) globalStyle.current.leftCheckBackgroundColor else globalStyle.current.leftUnCheckBackgroundColor
                                                        )
                                                ) {
                                                    Box(modifier = Modifier.width(20.dp))
                                                    Icon(
                                                        item.second,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(18.dp),
                                                        tint = if (index == checkItem) globalStyle.current.leftCheckFontColor else globalStyle.current.leftUnCheckFontColor,
                                                    )
                                                    Box(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        item.first,
                                                        color = if (index == checkItem) globalStyle.current.leftCheckFontColor else globalStyle.current.leftUnCheckFontColor,
                                                        fontSize = globalStyle.current.leftFontSize
                                                    )
                                                }
                                            }
                                            items(50) {
                                                Row(modifier = Modifier.fillMaxWidth()) { Text("111") }
                                            }

                                        }
                                        var show by remember { mutableStateOf(false) }
                                        var move by remember { mutableStateOf(false) }
                                        LaunchedEffect(scrollState.isScrollInProgress, move) {
                                            if (scrollState.isScrollInProgress || move) {
                                                show = true
                                            } else {
                                                delay(3000)
                                                show = false
                                            }
                                        }
                                        if (show) {
                                            VerticalScrollbar(
                                                modifier = Modifier.align(Alignment.CenterEnd)
                                                    .pointerInput(Unit) {
                                                        awaitEachGesture {
                                                            val awaitPointerEvent = awaitPointerEvent()
                                                            move = awaitPointerEvent.type== PointerEventType.Move
                                                        }

                                                    },
                                                adapter = rememberScrollbarAdapter(scrollState),
                                                style = ScrollbarStyle(
                                                    minimalHeight = 16.dp,
                                                    thickness = 8.dp,
                                                    shape = RoundedCornerShape(4.dp),
                                                    hoverDurationMillis = 300,
                                                    unhoverColor = globalStyle.current.scrollColor,
                                                    hoverColor = globalStyle.current.scrollCheckColor
                                                )
                                            )
                                        }

                                    }

                                }
                            }
                            Box {

                                val scrollState = rememberLazyListState()
                                LazyColumn(state = scrollState) {
                                    item {
                                        Surface(
                                            modifier = Modifier
                                                .height(250.dp)
                                                .background(globalStyle.current.contentBackgroundColor)
                                                .padding(horizontal = 20.dp),
                                            color = globalStyle.current.contentBackgroundColor
                                        ) {
                                            Swipe()

                                        }
                                    }
                                    items(20) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text("第$it")
                                            Box(modifier = Modifier.height(100.dp))
                                        }
                                    }
                                }
                                VerticalScrollbar(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    adapter = rememberScrollbarAdapter(scrollState),
                                    style = ScrollbarStyle(
                                        minimalHeight = 16.dp,
                                        thickness = 8.dp,
                                        shape = RoundedCornerShape(4.dp),
                                        hoverDurationMillis = 300,
                                        unhoverColor = globalStyle.current.scrollColor,
                                        hoverColor = globalStyle.current.scrollCheckColor
                                    )
                                )
                            }
                        }
                    }

                }
            }

        }


    }
}

