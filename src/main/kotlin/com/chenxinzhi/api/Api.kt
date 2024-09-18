package com.chenxinzhi.api

import com.chenxinzhi.model.search.Search
import com.chenxinzhi.utils.KtorHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/18
 */
object Api {

    suspend fun search():Result<Search> = KtorHttpClient.CLIENT.get {
        url("/search/suggest")
        parameters {
            append("keywords","海阔天空")
        }
    }

        .body()

}