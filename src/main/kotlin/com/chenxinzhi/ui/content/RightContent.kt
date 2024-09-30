package com.chenxinzhi.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RightContent(searchKey: MutableStateFlow<String>,musicId: MutableStateFlow<String>) {
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
       val key by searchKey.collectAsState()
        SearchListContent(key,musicId)
    }

}
