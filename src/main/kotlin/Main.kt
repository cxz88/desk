import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.chenxinzhi.ui.content.Content
import com.chenxinzhi.ui.content.LeftContent
import com.chenxinzhi.ui.content.RightContent
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle


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
        App(state, ::exitApplication)
    }
}

@Composable
private fun FrameWindowScope.App(
    state: WindowState,
    closeApp: () -> Unit,
) {

    CompositionLocalProvider(globalStyle provides GlobalStyle) {
        MaterialTheme {
            Content(state = state, { closeApp() }) {
                Row {
                    LeftContent()
                    RightContent()
                }
            }


        }

    }
}




