package com.chenxinzhi.utils

import com.chenxinzhi.model.base.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/18
 */
object KtorHttpClient {
    val CLIENT = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url("http://127.0.0.1:3000/")
        }


    }

    suspend inline fun <reified T> getOrDefault(builder: HttpRequestBuilder.() -> Unit): Response<T> {
        return try {
            CLIENT.get(builder).body()
        } catch (e: Exception) {
            Response(999, null)
        }
    }
}