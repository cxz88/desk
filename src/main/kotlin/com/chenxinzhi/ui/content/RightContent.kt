package com.chenxinzhi.ui.content

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.chenxinzhi.route.ContentRoute
import com.chenxinzhi.ui.page.DownloadPage
import com.chenxinzhi.ui.page.LovePage
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun RightContent(searchKey: MutableStateFlow<String>, musicId: MutableStateFlow<String>, navigator: Navigator) {
    Box(modifier = Modifier.fillMaxSize().graphicsLayer {
        clip = true
    }, contentAlignment = Alignment.Center) {
        val key by searchKey.collectAsState()
        LaunchedEffect(key) {
            navigator.navigate(ContentRoute.search)
        }
        NavHost(
            navigator = navigator,
            navTransition = NavTransition(createTransition =
            slideIn {
                IntOffset(-it.width, 0)
            }, pauseTransition =
                slideOut {
                    IntOffset(it.width, 0)
                }

            ),
            initialRoute = ContentRoute.search
        ) {
            scene(ContentRoute.search) {
                SearchListContent(key, musicId)
            }
            scene(ContentRoute.love) {
                LovePage()
            }
            scene(ContentRoute.download) {
                DownloadPage()
            }
        }
    }

}
