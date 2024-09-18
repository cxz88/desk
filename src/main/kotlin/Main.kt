
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.chenxinzhi.api.Api
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
import java.awt.Toolkit

val sqlDriver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")


fun main() =
    application {
        val lycContent = remember { MutableStateFlow("") }
        val lycDeskShow = remember { MutableStateFlow(true) }
        val c = rememberCoroutineScope()
        c.launch {
            println(Api.search())
        }
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
        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = state,
            transparent = true,
            title = "",
            resizable = false,
        ) {
            PreComposeApp {
                App(state, ::exitApplication, lycContent, lycDeskShow)
            }
        }

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
            Window(
                state = windowState,
                onCloseRequest = ::exitApplication,
                title = "",
                transparent = true,
                undecorated = true,
                resizable = false,
                alwaysOnTop = true,
            ) {

                CompositionLocalProvider(globalStyle provides GlobalStyle) {
                    deskLyc(lycContent, lycDeskShow, windowState)
                }

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
            Content(state = state, { closeApp() }, lycContent, lycDeskShow) {
                Row {
                    LeftContent()
                    RightContent()
                }
            }


        }

    }
}




