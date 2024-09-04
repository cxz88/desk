import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Component
import java.awt.event.MouseEvent
import java.awt.event.WindowEvent
import java.awt.event.WindowListener


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val state = rememberWindowState()
    Window(onCloseRequest = ::exitApplication, undecorated = true, state = state, transparent = true) {
        Box(
            modifier = Modifier.background(Color.Transparent).fillMaxSize().padding(0.dp, 0.dp, 4.dp, 4.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(10.dp),
                elevation = 2.dp,
                color = Color.Transparent,
                border = BorderStroke(.8.dp, Color(0x44abb0b3))
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xff2b2d30),
                                Color(0xff2b2d30),
                                Color(0xff354b54),
                                Color(0xff3c3f41),
                                Color(0xff3c3f41),
                                Color(0xff2b2d30),
                                Color(0xff2b2d30)
                            )
                        )
                    ).fillMaxSize()
                ) {
                    Column {
                        Box(
                            modifier = Modifier.background(
                                Color.Transparent
                            ).fillMaxWidth()
                                .pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        var preXOnScreen = 0
                                        var preYOnScreen = 0
                                        var startX = 0
                                        var startY = 0
                                        var posX = 0.dp
                                        var posY = 0.dp
                                        while (true) {
                                            val awaitPointerEvent = awaitPointerEvent()
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
                                                if (awaitPointerEvent.button.isPrimary) {
                                                    val nativeEvent = awaitPointerEvent.nativeEvent
                                                    if (nativeEvent is MouseEvent) {
                                                        val xOnScreen = nativeEvent.xOnScreen
                                                        val yOnScreen = nativeEvent.yOnScreen
                                                        val xOffset = xOnScreen - preXOnScreen
                                                        val yOffset = yOnScreen - preYOnScreen
                                                        state.position =
                                                            WindowPosition(
                                                                posX + xOffset.toDp() + startX.dp,
                                                                posY + yOffset.toDp() + startY.dp
                                                            )
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                                .height(35.dp)
                        ) {
                            val m = remember { MutableInteractionSource() }
                            var active by remember { mutableStateOf(true) }
                            val hover by m.collectIsHoveredAsState()
                            window.addWindowListener(object : WindowListener {
                                override fun windowOpened(e: WindowEvent?) {

                                }

                                override fun windowClosing(e: WindowEvent?) {

                                }

                                override fun windowClosed(e: WindowEvent?) {

                                }

                                override fun windowIconified(e: WindowEvent?) {

                                }

                                override fun windowDeiconified(e: WindowEvent?) {

                                }

                                override fun windowActivated(e: WindowEvent?) {
                                    active = true
                                }

                                override fun windowDeactivated(e: WindowEvent?) {
                                    active = false
                                }

                            })

                            val close by animateColorAsState(
                                targetValue = if (active) {
                                    Color(0xffff5f57)
                                } else {
                                    Color(0xFF53636a)
                                }
                            )
                            val min by animateColorAsState(
                                targetValue = if (active) {
                                    Color(0xfffdbb2e)
                                } else {
                                    Color(0xFF53636a)
                                }
                            )
                            Row(modifier = Modifier.padding(15.dp, 12.dp, 0.dp, 0.dp)) {
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(6.0.dp))
                                        .background(color = close).size(12.dp).hoverable(
                                            m
                                        ).clickable { exitApplication() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (hover) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(10.dp),
                                            tint = Color.Black
                                        )
                                    }
                                }
                                Box(modifier = Modifier.size(8.dp)) {}
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(6.0.dp))
                                        .background(color = min).size(12.dp).hoverable(
                                            m
                                        ).clickable { window.isMinimized = true; },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (hover) {
                                        Box(modifier = Modifier.size(6.dp, 1.dp).background(Color.Black)) {
                                        }
                                    }
                                }
                            }

                        }
                        Box(
                            modifier = Modifier.background(
                                Color.Transparent
                            ).fillMaxSize()
                        ) {
                            Surface(
                                color = Color.Transparent,
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                LazyColumn {

                                }
                            }
                        }
                    }
                }


            }
        }


    }
}

object Mouse {
    var startX: Int = 0
    var startY: Int = 0
    var oldX: Int = 0
    var oldY: Int = 0
    var oldXDP: Dp = 0.dp
    var oldYDP: Dp = 0.dp
    var newX: Int = 0
    var newY: Int = 0

}
