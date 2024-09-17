import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.use
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.chenxinzhi.repository.Database
import com.chenxinzhi.ui.content.Content
import com.chenxinzhi.ui.content.LeftContent
import com.chenxinzhi.ui.content.RightContent
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.PreComposeApp
import java.awt.Toolkit

val sqlDriver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")
fun main() =
    application {
        val lycContent = MutableStateFlow("")
        val lycDeskShow = MutableStateFlow(true)
        val ldShow by lycDeskShow.collectAsState()
        try {
            Database.Schema.create(sqlDriver)
        } catch (_: Exception) {

        }
        val state = rememberWindowState(size = DpSize(1000.dp, 700.dp))
        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = state,
            transparent = true,
            title = "",
            resizable = false,
        ) {
            PreComposeApp {
                App(state, ::exitApplication, lycContent)
            }
        }
        val LycWindowState = rememberWindowState(size = DpSize(1000.dp, 100.dp))
        if (ldShow) {
            Window(
                state = LycWindowState,
                onCloseRequest = ::exitApplication,
                title = "",
                transparent = true,
                undecorated = true,
                resizable = false,
                alwaysOnTop = true,
            ) {
                val screenSize = Toolkit.getDefaultToolkit().screenSize
                val x = (screenSize.width.dp - 1000.dp) / 2
                val y = screenSize.height.dp - 190.dp
                LycWindowState.position = WindowPosition(x, y)
                CompositionLocalProvider(globalStyle provides GlobalStyle) {
                    deskLyc(lycContent,lycDeskShow)
                }

            }
        }
    }

@Composable
fun deskLyc(lycContent: MutableStateFlow<String>, lycDeskShow: MutableStateFlow<Boolean>) {
    var nowContent by remember { mutableStateOf("") }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val collectIsHoveredAsState by interactionSource.collectIsHoveredAsState()
    val bg by animateColorAsState(if (collectIsHoveredAsState) globalStyle.current.deskLycBackGround else Color.Transparent)
    Box(modifier = Modifier
        .graphicsLayer {
            shape = RoundedCornerShape(8.dp)
            clip = true
        }
        .background(color = bg)
        .fillMaxSize()
        .hoverable(interactionSource)
        .drawWithCache {
          val imageBitmap=  javaClass.getResourceAsStream("/image/ic_window_close.webp")?.use { loadImageBitmap(it) }

            onDrawBehind {
                imageBitmap?.let {
                    drawImage(it)
                }
            }
        },
        contentAlignment = Alignment.Center

    ) {
        Text(nowContent, color = Color.White, fontSize = 20.sp)
        LaunchedEffect(Unit) {
            lycContent.collect {
                nowContent = it
            }
        }
    }
}

@Composable
private fun FrameWindowScope.App(
    state: WindowState,
    closeApp: () -> Unit,
    lycContent: MutableStateFlow<String>,
) {

    CompositionLocalProvider(globalStyle provides GlobalStyle) {
        MaterialTheme {
            Content(state = state, { closeApp() }, lycContent) {
                Row {
                    LeftContent()
                    RightContent()
                }
            }


        }

    }
}




