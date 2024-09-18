package com.chenxinzhi.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
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
            })
        }
        defaultRequest {
            host="http://127.0.0.1:3000"
        }
    }
}