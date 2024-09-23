package com.chenxinzhi.ui.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenxinzhi.api.Api
import com.chenxinzhi.ui.style.GlobalStyle
import com.chenxinzhi.ui.style.globalStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RowScope.RightBar(
    searchList: MutableStateFlow<List<String>>,
    showSearchTip: MutableStateFlow<Boolean>,
    searchKeyList: MutableStateFlow<String>,
    closeFill: () -> Unit,
    content: @Composable () -> Unit
) {
    content()
    Row(
        modifier = Modifier.offset { IntOffset(0, 0) }.weight(1f).fillMaxHeight().padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom

    ) {
        var searchText by remember {
            mutableStateOf("")
        }
        Column {
            Row(
                modifier = Modifier
                    .offset { IntOffset(0, -4.dp.roundToPx()) }.clip(shape = RoundedCornerShape(16.dp))
                    .width(150.dp).height(25.dp).background(globalStyle.current.searchColor)
                    .padding(start = 5.dp, end = 6.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                val searchKey = remember {
                    MutableStateFlow("")
                }
                LaunchedEffect(Unit) {
                    searchKey.collectLatest {
                        //发送网络请求更新
                        searchList.value = (Api.search(it).data ?: listOf()).map { str ->
                            try {
                                str.substring(8, str.indexOf("SNUM"))
                            } catch (e: Exception) {
                                ""
                            }
                        }


                    }
                }
                val fm =  LocalFocusManager.current
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = globalStyle.current.searchIconColor,
                    modifier = Modifier
                        .size(20.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = Color.Transparent,
                        backgroundColor = Color(0xff668bb1)
                    )
                ) {
                    val focusRequester = remember { FocusRequester() }
                    BasicTextField(
//                leadingIcon = {
//                    Icon(
//                        Icons.Default.Search,
//                        contentDescription = null,
//                        tint = globalStyle.current.searchIconColor,
//                        modifier = Modifier
//                            .size(20.dp)
//                    )
//                },
                        modifier = Modifier.onFocusChanged {
                            showSearchTip.value = it.isFocused
                        },
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            //搜索
                            searchKey.value = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // 设置 IME 动作为 Done (回车)
                        ),

                        keyboardActions = KeyboardActions(
                            onDone = {
                                //发送请求
                                searchKeyList.value = "$searchText,${System.currentTimeMillis()}"
                                //删除焦点
                                fm.clearFocus()
                                closeFill()
                            }
                        ),
                        singleLine = true,
                        maxLines = 1,
                        cursorBrush = SolidColor(Color.White),
                        textStyle = TextStyle(fontSize = 12.sp, color = Color(0xffa5a5a5)),
                        decorationBox = { innerTextField ->

                            if (searchText.isBlank()) {
                                Text(
                                    "输入歌曲名搜索",
                                    color = globalStyle.current.textUnCheckColor,
                                    fontSize = GlobalStyle.defaultSearchFontSize,
                                    lineHeight = GlobalStyle.defaultSearchFontSize,
                                    modifier = Modifier.pointerHoverIcon(PointerIcon.Text)
                                )
                            }
                            innerTextField()

                        }

//                placeholder = {
//                    Text(
//                        "搜索",
//                        color = globalStyle.current.textUnCheckColor,
//                        fontSize = GlobalStyle.defaultSearchFontSize,
//                        modifier = Modifier
//                    )
//                },
//                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),


                    )
                }

            }
        }
        Box(modifier = Modifier.width(10.dp))
        Row(
            modifier = Modifier.offset { IntOffset(0, -6.dp.roundToPx()) }.width(180.dp),
            verticalAlignment = Alignment.Bottom
        ) {}
// {
////            val focusManager = LocalFocusManager.current
//            Icon(
//                Icons.Outlined.Settings,
//                contentDescription = null,
//                tint = globalStyle.current.topBarRightColor,
//                modifier = Modifier.size(19.dp).clickable {
////                    focusManager.clearFocus()
//                }
//            )
//            Box(modifier = Modifier.width(16.dp))
//            Icon(
//                Icons.Outlined.Email,
//                contentDescription = null,
//                tint = globalStyle.current.topBarRightColor,
//                modifier = Modifier.size(19.dp)
//            )
//            Box(modifier = Modifier.width(16.dp))
//            Icon(
//                painterResource("image/ic_theme.webp"),
//                contentDescription = null,
//                tint = globalStyle.current.topBarRightColor,
//                modifier = Modifier.size(18.dp)
//            )
//            Box(modifier = Modifier.width(16.dp))
//            Icon(
//                painterResource("image/ic_screen_max.webp"),
//                contentDescription = null,
//                tint = globalStyle.current.topBarRightColor,
//                modifier = Modifier.size(18.dp)
//            )
//            Box(modifier = Modifier.width(16.dp))
//        }
    }
}