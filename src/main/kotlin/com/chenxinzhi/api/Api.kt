package com.chenxinzhi.api

import com.chenxinzhi.model.base.Response
import com.chenxinzhi.model.search.Search
import com.chenxinzhi.utils.KtorHttpClient
import io.ktor.client.request.*

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/18
 */
object Api {

    suspend fun search(): Response<Search> = KtorHttpClient.getOrDefault {
        url("search1/suggest")
        parameter("keywords", "海阔天空")

    }


}