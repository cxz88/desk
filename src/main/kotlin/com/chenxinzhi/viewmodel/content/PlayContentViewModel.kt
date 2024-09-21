package com.chenxinzhi.viewmodel.content

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.chenxinzhi.utils.convertToSeconds
import moe.tlaster.precompose.viewmodel.ViewModel


class PlayContentViewModel : ViewModel() {
    var lycList by mutableStateOf(
        "[00:00.00] 暂无歌词"
            .split("\n").associate {

                (if (it.length > 8) {
                    it.substring(1, 9)
                } else {
                    it
                }) to if (it.length > 8) {
                    it.substring(10)
                } else {
                    ""
                }
            }
            .filter {
                it.key.isNotBlank()
            }
            .map {
                convertToSeconds(it.key) to it.value
            }.toList()
    )



}