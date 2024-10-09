package com.chenxinzhi.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.chenxinzhi.api.Api
import com.chenxinzhi.model.search.Abslist
import com.chenxinzhi.utils.convertToSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


object SearchListViewModel : ViewModel() {
    var loading by mutableStateOf(false)
    var lazyListState = LazyListState()

    var listContent = mutableStateListOf<Abslist>()
    var job: Job = Job()

    var preSearch by mutableStateOf("")

    fun loadContentList(searchStr: String) {
        if (preSearch == searchStr) {
            return
        } else {
            preSearch = searchStr
        }
        job.children.forEach {
            it.cancel()
        }
        viewModelScope.launch(Dispatchers.IO + job) {
            loading = true
            listContent.clear()
            //显示加载动画,进行网络数据加载
            var count = 0
            val c = count++
            if (searchStr.isBlank()) {
                loading = false
                return@launch
            }
            val s = Api.page(searchStr, c)
            var l = s?.abslist ?: listOf()
            var countW = 0
            while (l.isEmpty() && countW < 30) {
                val s2 = Api.page(searchStr, c)
                l = s2?.abslist ?: listOf()
                countW++
            }
            listContent += l
            loading = false
            s?.let {
                while (it.tOTAL.toInt() > listContent.size) {
                    val pn = count++
                    val s1 = Api.page(searchStr, pn)
                    var abslists: List<Abslist> = s1?.abslist ?: listOf()
                    var countb = 0
                    while (abslists.isEmpty() && countb < 30) {
                        val s4 = Api.page(searchStr, pn)
                        abslists = s4?.abslist ?: listOf()
                        countb++
                    }
                    listContent += abslists

                }
            }
        }
    }


}