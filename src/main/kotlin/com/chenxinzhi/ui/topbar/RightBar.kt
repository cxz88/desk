package com.chenxinzhi.ui.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle

@Composable
fun RowScope.RightBar(content: @Composable () -> Unit) {
    content()
    Row(
        modifier = Modifier.offset { IntOffset(0, 0) }.weight(1f).fillMaxHeight().padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom

    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(0, -4.dp.roundToPx()) }.clip(shape = RoundedCornerShape(16.dp))
                .width(150.dp).height(25.dp).background(globalStyle.current.searchColor)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = globalStyle.current.searchIconColor,
                modifier = Modifier
                    .offset { IntOffset(4.dp.roundToPx(), 4.dp.roundToPx()) }
                    .size(20.dp))
            Text(
                "搜索",
                color = globalStyle.current.textUnCheckColor,
                fontSize = GlobalStyle.defaultSearchFontSize,
                modifier = Modifier.offset { IntOffset(25.dp.roundToPx(), 0) }
            )

        }
        Box(modifier = Modifier.width(10.dp))
        Row(
            modifier = Modifier.offset { IntOffset(0, -6.dp.roundToPx()) }.fillMaxHeight(),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
                tint = globalStyle.current.topBarRightColor,
                modifier = Modifier.size(19.dp)
            )
            Box(modifier = Modifier.width(16.dp))
            Icon(
                Icons.Outlined.Email,
                contentDescription = null,
                tint = globalStyle.current.topBarRightColor,
                modifier = Modifier.size(19.dp)
            )
            Box(modifier = Modifier.width(16.dp))
            Icon(
                painterResource("image/ic_theme.webp"),
                contentDescription = null,
                tint = globalStyle.current.topBarRightColor,
                modifier = Modifier.size(18.dp)
            )
            Box(modifier = Modifier.width(16.dp))
            Icon(
                painterResource("image/ic_screen_max.webp"),
                contentDescription = null,
                tint = globalStyle.current.topBarRightColor,
                modifier = Modifier.size(18.dp)
            )
            Box(modifier = Modifier.width(16.dp))
        }
    }
}