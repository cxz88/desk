import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.chenxinzhi.repository.Database
import com.chenxinzhi.sqlservice.FuncEnum
import com.chenxinzhi.sqlservice.getByKey
import com.chenxinzhi.sqlservice.updateByKey
import com.chenxinzhi.ui.content.Content
import com.chenxinzhi.ui.content.LeftContent
import com.chenxinzhi.ui.content.RightContent
import com.chenxinzhi.ui.desk.deskLyc
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import java.awt.*

val sqlDriver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${System.getProperty("user.home")}/test.db")


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        Dialog(Frame(), e.message ?: "错误").apply {
            layout = FlowLayout()
            val label = TextArea(e.toString())
            add(label)
            val button = Button("确定").apply {
                addActionListener { dispose() }
            }
            add(button)
            setSize(500, 500)
            isVisible = true
        }
    }
    application {
        val lycContent = remember { MutableStateFlow("") }
        val lycDeskShow = remember { MutableStateFlow(true) }
        val c = rememberCoroutineScope()
        val ldShow by lycDeskShow.collectAsState()
        try {
            Database.Schema.create(sqlDriver)
        } catch (_: Exception) {

        }

        val state = rememberWindowState(size = DpSize(1000.dp, 700.dp))
        remember {
            runBlocking {
                val screenSize = Toolkit.getDefaultToolkit().screenSize
                val x = (screenSize.width.dp - 1000.dp) / 2
                val y = (screenSize.height.dp - 700.dp) / 2
                val xy = getByKey(FuncEnum.mainPost, "${x.value},${y.value}")
                val split = xy.split(",")
                state.position = WindowPosition(split[0].toFloat().dp, split[1].toFloat().dp)
            }
        }
        val post = state.position
        remember(post) {
            //保存
            c.launch {
                updateByKey(FuncEnum.mainPost, "${post.x.value},${post.y.value}")
            }

        }

        Window(onCloseRequest = ::exitApplication,
            state = state,
            title = "",
            decoration = WindowDecoration.Undecorated(),
            transparent = true,
            resizable = false,
            content = {
                Tray(
                    icon = ColorPainter(Color.Transparent),
                   tooltip = lycContent.collectAsState().value

                )
                PreComposeApp {
                    App(state, ::exitApplication, lycContent, lycDeskShow)
                }
            })

        val windowState = rememberWindowState(size = DpSize(1000.dp, 100.dp))
        remember {
            runBlocking {
                val screenSize = Toolkit.getDefaultToolkit().screenSize
                val x = (screenSize.width.dp - 1000.dp) / 2
                val y = screenSize.height.dp - 190.dp
                val xy = getByKey(FuncEnum.LycPost, "${x.value},${y.value}")
                val split = xy.split(",")
                windowState.position = WindowPosition(split[0].toFloat().dp, split[1].toFloat().dp)
            }
        }
        val lycPost = windowState.position
        remember(lycPost) {
            //保存
            c.launch {
                updateByKey(FuncEnum.LycPost, "${lycPost.x.value},${lycPost.y.value}")
            }
        }
        if (ldShow) {
            Window(onCloseRequest = ::exitApplication,
                state = windowState,
                title = "",
                decoration = WindowDecoration.Undecorated(),
                transparent = true,
                resizable = false,
                alwaysOnTop = true,
                content = {
                    CompositionLocalProvider(globalStyle provides GlobalStyle) {
                        deskLyc(lycContent, lycDeskShow, windowState)
                    }
                })
        }
    }

}

@Composable
private fun FrameWindowScope.App(
    state: WindowState,
    closeApp: () -> Unit,
    lycContent: MutableStateFlow<String>,
    lycDeskShow: MutableStateFlow<Boolean>,
) {

    CompositionLocalProvider(globalStyle provides GlobalStyle) {
        MaterialTheme {
            val searchKey = remember { MutableStateFlow("") }
            val closeFlow = remember { MutableStateFlow(false) }
            val navigator = rememberNavigator()
            Content(state = state, { closeApp() }, lycContent, lycDeskShow, searchKey, closeFlow) {
                Row {
                    LeftContent(navigator)
                    RightContent(searchKey, it,navigator)
                }
            }


        }

    }
}




