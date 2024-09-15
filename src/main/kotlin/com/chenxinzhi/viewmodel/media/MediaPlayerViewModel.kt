package com.chenxinzhi.viewmodel.media

import androidx.compose.runtime.*
import javafx.scene.media.MediaPlayer
import moe.tlaster.precompose.viewmodel.ViewModel


class MediaPlayerViewModel : ViewModel() {
    var isReady by mutableStateOf(false)
    var isPause by mutableStateOf(false)
    var duration by mutableStateOf(0f)
    var volume by mutableStateOf(1f)
    var mediaPlayerState by mutableStateOf<MediaPlayer?>(null)
    var currentTime by  mutableStateOf(0f)
    val progress
       by derivedStateOf {
            if (currentTime == 0f && duration == 0f) {
                0f
            } else {
                currentTime / duration
            }
        }
    var isWait by mutableStateOf(false)
    var showPaint by  mutableStateOf(false)





}
