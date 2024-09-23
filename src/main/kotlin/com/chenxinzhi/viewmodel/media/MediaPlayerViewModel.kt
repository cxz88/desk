package com.chenxinzhi.viewmodel.media

import androidx.compose.runtime.*
import com.chenxinzhi.sqlservice.FuncEnum
import com.chenxinzhi.sqlservice.getByKey
import com.chenxinzhi.sqlservice.updateByKey
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

@Stable
class MediaPlayerViewModel : ViewModel() {
    var isReady by mutableStateOf(false)
    var isPause by mutableStateOf(false)
    var duration by mutableStateOf(0f)
    var volume by mutableStateOf(1f)
    var mediaPlayerState by mutableStateOf<MediaPlayer?>(null)
    var currentTime by mutableStateOf(0f)
    val progress
            by derivedStateOf {
                if (currentTime == 0f && duration == 0f) {
                    0f
                } else {
                    currentTime / duration
                }
            }
    var isWait by mutableStateOf(false)
    var showPaint by mutableStateOf(false)
    val playModeList = listOf(
        "image/ic_play_mode_loop.webp" to "循环播放",
        "image/ic_play_mode_random.webp" to "随机播放",
        "image/ic_play_mode_single.webp" to "单曲循环"
    )
    private var _nowPlayerModel by mutableStateOf(0)

    val nowPlayerModel
        get() = _nowPlayerModel

    var isReadyAll = mutableStateOf(false)


    private var _showDeskLyc by mutableStateOf(false)

    val showDeskLyc
        get() = _showDeskLyc


    var nowPlayIndex by mutableStateOf(runBlocking {
        val s = getByKey(FuncEnum.PLAY_LIST, "").split(":%%19969685426854***")
        val s1 = getByKey(FuncEnum.MUSIC_ID, "")
        if (s.size == 1 && s[0].isBlank()) {
            -1
        } else {
            s.map { it.split(",")[0] }.lastIndexOf(s1.split(",")[0])
        }
    })

    fun setShowDeskLyc(value: Boolean) {
        _showDeskLyc = value
        if (isReady) {
            viewModelScope.launch {
                updateByKey(FuncEnum.SHOW_DESK_LYC, value.toString())
            }
        }
    }


    fun setNowPlayerModel(value: Int) {
        val v = value % (playModeList.size)
        _nowPlayerModel = v
        if (isReady) {
            viewModelScope.launch {
                updateByKey(FuncEnum.PLAY_MODEL, v.toString())
            }
        }
    }


    init {
        viewModelScope.launch {
            _nowPlayerModel = getByKey(FuncEnum.PLAY_MODEL, "0").toInt()
            _showDeskLyc = getByKey(FuncEnum.SHOW_DESK_LYC, false.toString()).toBoolean()
        }
    }


}
