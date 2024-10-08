package com.chenxinzhi.ui.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chenxinzhi.api.Api
import com.chenxinzhi.model.search.Abslist
import com.chenxinzhi.ui.Loading
import com.chenxinzhi.ui.style.globalStyle
import com.chenxinzhi.viewmodel.SearchListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.viewmodel.viewModel

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/21
 */
@Composable
fun SearchListContent(
    search: String, musicId: MutableStateFlow<String>, searchListViewModel: SearchListViewModel = viewModel {
        SearchListViewModel
    }
) {
    val searchStr = search.split(",")[0]
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 62.dp), contentAlignment = Alignment.Center) {
        LaunchedEffect(searchStr) {
            searchListViewModel.loadContentList(searchStr)
        }
        AnimatedContent(searchListViewModel.loading) {
            if (it) {
                Loading()
            } else {
                Column {
                    Row(modifier = Modifier.padding(start = 30.dp, end = 30.dp).padding(vertical = 15.dp)) {
                        Text(
                            "歌名",
                            color = Color(0xffb2b2b2),
                            fontSize = 14.sp,
                            modifier = Modifier.width(220.dp),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "歌手",
                            color = Color(0xffb2b2b2),
                            fontSize = 14.sp,
                            modifier = Modifier.width(220.dp),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "专辑",
                            color = Color(0xffb2b2b2),
                            fontSize = 14.sp,
                            modifier = Modifier.width(220.dp),
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text("时长", color = Color(0xffb2b2b2), fontSize = 14.sp)
                    }
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            state = searchListViewModel.lazyListState, modifier = Modifier.fillMaxSize()
                        ) {

                            items(searchListViewModel.listContent.size) { index ->
                                searchListViewModel.listContent[index].let { abs ->
                                    val s = remember { MutableInteractionSource() }
                                    val h by s.collectIsHoveredAsState()
                                    Box(
                                        modifier = Modifier
                                            .hoverable(s)
                                            .background(
                                                if (index % 2 == 0) Color(0xff252525) else Color(
                                                    0xff292929
                                                )
                                            ).background(
                                                if (h) {
                                                    Color(0xff323232)
                                                } else {
                                                    Color.Transparent
                                                }
                                            ).fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        val f = LocalFocusManager.current
                                        Row(modifier = Modifier.pointerInput(Unit) {
                                            detectTapGestures(onDoubleTap = {
                                                f.clearFocus()
                                                musicId.value =
                                                    "${abs.dCTARGETID},${
                                                        if (abs.webAlbumpicShort.isBlank()) {
                                                            "https://img1.kuwo.cn/star/starheads/${abs.webArtistpicShort}".replace(
                                                                "/120/",
                                                                "/1000/"
                                                            )
                                                        } else {
                                                            "https://img2.kuwo.cn/star/albumcover/${abs.webAlbumpicShort}".replace(
                                                                "/120/",
                                                                "/1000/"
                                                            )
                                                        }

                                                    },${abs.nAME},${abs.aRTIST},${System.currentTimeMillis()}"
                                            })
                                        }) {
                                            val s = abs.dURATION.toInt()
                                            val ss = s % 60
                                            val m = s / 60
                                            Text(
                                                abs.nAME,
                                                color = Color(0xffb2b2b2),
                                                fontSize = 14.sp,
                                                modifier = Modifier.width(220.dp).padding(end = 10.dp)
                                            )
                                            Text(
                                                abs.aRTIST,
                                                color = Color(0xffb2b2b2),
                                                fontSize = 14.sp,
                                                modifier = Modifier.width(220.dp).padding(end = 10.dp)
                                            )
                                            Text(
                                                abs.aLBUM,
                                                color = Color(0xffb2b2b2),
                                                fontSize = 14.sp,
                                                modifier = Modifier.width(220.dp).padding(end = 10.dp)
                                            )
                                            Text("$m:$ss", color = Color(0xffb2b2b2), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                        VerticalScrollbar(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            adapter = rememberScrollbarAdapter(searchListViewModel.lazyListState),
                            style = ScrollbarStyle(
                                minimalHeight = 16.dp,
                                thickness = 8.dp,
                                shape = RoundedCornerShape(4.dp),
                                hoverDurationMillis = 300,
                                unhoverColor = globalStyle.current.scrollColor,
                                hoverColor = globalStyle.current.scrollCheckColor
                            )
                        )
                    }
                }
            }
        }

    }

}