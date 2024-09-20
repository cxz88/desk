package com.chenxinzhi.api

import com.chenxinzhi.model.base.KuWoResponse
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

    suspend fun search(key: String): KuWoResponse<List<String>> = withContext(Dispatchers.IO){
        KtorHttpClient.getAndFallBack {
            url("search/searchKey")
            parameter("key", key)

        }
    }


}