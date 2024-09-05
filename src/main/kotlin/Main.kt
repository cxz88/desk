import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.chenxinzhi.ui.GlobalStyle
import com.chenxinzhi.ui.globalStyle
import com.chenxinzhi.ui.App


fun main() = application {
    val state = rememberWindowState(size = DpSize(1200.dp, 700.dp))
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        state = state,
        transparent = true,
        title = ""
    ) {
        CompositionLocalProvider(globalStyle provides GlobalStyle) {
            MaterialTheme {
                App(state = state, { exitApplication() }) {
                    Column {
                        Box(
                            Modifier.fillMaxWidth()
                                .height(80.dp)
                                .background(globalStyle.current.backgroundColor)
                        ) {

                        }
                    }
                }
            }
        }


    }
}

