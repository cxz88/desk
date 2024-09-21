package com.chenxinzhi.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.ViewModel

class LoadingViewModel : ViewModel() {


    private val white1 = Color(0xFFCCCCCC)
    private val white2 = Color(0xD6CCCCCC)
    private val white3 = Color(0xB8CCCCCC)
    private val white4 = Color(0x99CCCCCC)
    private val white5 = Color(0x7ACCCCCC)
    private val white6 = Color(0x5CCCCCCC)
    private val white7 = Color(0x3DCCCCCC)
    private val white8 = Color(0x1FCCCCCC)

    val mColor = mutableStateListOf(
        white1,
        white2,
        white3,
        white4,
        white5,
        white6,
        white7,
        white8,
    )


    init {
        mColor[0] = white1
        mColor[1] = white2
        mColor[2] = white3
        mColor[3] = white4
        mColor[4] = white5
        mColor[5] = white6
        mColor[6] = white7
        mColor[7] = white8
    }
    suspend fun start() {
        while (true) {
            val data = mColor.removeAt(mColor.size - 1)
            mColor.add(0, data)
            delay(100)

        }

    }


}
