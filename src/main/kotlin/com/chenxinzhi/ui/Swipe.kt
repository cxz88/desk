package com.chenxinzhi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LookaheadScope.Swipe() {

    val com = remember {
        movableContentWithReceiverOf<LookaheadScope> {
            Box(modifier = Modifier.fillMaxSize().padding(top = 20.dp, bottom = 20.dp)
                ){
                Image(painterResource("image/1.jpg"), modifier = Modifier.clip(RoundedCornerShape(10.dp)).align(alignment = Alignment.Center)
                    .fillMaxHeight().width(550.dp), contentDescription = null, contentScale = ContentScale.Crop)
            }
        }
    }
    com()


}
