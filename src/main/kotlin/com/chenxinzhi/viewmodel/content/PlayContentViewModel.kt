package com.chenxinzhi.viewmodel.content

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chenxinzhi.utils.convertToSeconds
import moe.tlaster.precompose.viewmodel.ViewModel

class PlayContentViewModel : ViewModel() {
    val lycList by mutableStateOf(
        "[00:00.00] 作词 : 潘云安\n[00:01.00] 作曲 : 潘云安\n[00:02.00] 编曲 : 告五人\n[00:18.23]\n[00:46.82]形同虚设的时间\n[00:50.32]在你眼里成为了无限\n[00:54.19]青春充满了不眠\n[00:57.71]是为了追寻更多的明天\n[01:13.32]\n[01:16.91]好似无尽的灯街\n[01:20.29]从不分你我照亮在心间\n[01:24.03]夜已承载心无眠\n[01:27.79]再巨大的伤悲皆已成灰\n[01:35.52]\n[01:45.98]如果你还没有睡\n[01:49.44]如果我还不停追\n[01:53.15]如果清醒是种罪\n[01:56.93]就把誓言带走 换承诺不回\n[02:01.31]如果你就是一切\n[02:04.35]如果我就是绝对\n[02:08.12]如果清醒是种罪\n[02:12.12]就让爱去蔓延 成全每个夜\n[02:18.38]\n[02:46.25]时过境迁的伤悲\n[02:49.46]搭配快乐的宣泄\n[02:53.19]如果清醒是种罪\n[02:56.83]你会不会怨怼 将就的明天\n[03:01.40]如果你就是一切\n[03:04.52]如果我就是绝对\n[03:08.29]如果清醒是种罪\n[03:11.97]就让爱去蔓延 成全每个夜\n[03:18.28]\n[03:31.45]记住激情的滋味\n[03:34.49]记住流泪的画面\n[03:38.23]如果清醒是种罪\n[03:41.98]就拿偏执的一切\n[03:44.50]放弃无聊的称谓\n[03:46.55]如果你真是一切\n[03:49.43]如同我真是绝对\n[03:53.09]如果夜留下暧昧\n[03:57.46]让你我不再挂念\n[03:59.42]最后成全每个谁\n[04:14.24]\n"
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