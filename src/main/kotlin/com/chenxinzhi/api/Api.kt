package com.chenxinzhi.api

import com.chenxinzhi.model.base.KuWoResponse
import com.chenxinzhi.model.search.SearchList
import com.chenxinzhi.utils.KtorHttpClient
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/18
 */
object Api {

    suspend fun search(key: String): KuWoResponse<List<String>> = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack(KuWoResponse()) {
            url("https://www.kuwo.cn/openapi/v1/www/search/searchKey")
            parameter("key", key)

        }!!
    }

    suspend fun page(key: String, pn: Int = 0, rn: Int = 20): SearchList? = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack<SearchList?>(null) {
            url("https://www.kuwo.cn/search/searchMusicBykeyWord")
            parameter("all", key)
            parameter("ft", "music")
            parameter("encoding", "utf8")
            parameter("pn", pn)
            parameter("rn", rn)
            parameter("rformat", "json")
            parameter("mobi", 1)

        }
    }


}