package com.chenxinzhi.viewmodel.media

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import moe.tlaster.precompose.viewmodel.ViewModel

class ProcessIndicatorViewModel: ViewModel() {
    var processInner by mutableStateOf(0f)
    var useInner by mutableStateOf(false)
    val interactionSource = MutableInteractionSource()



}