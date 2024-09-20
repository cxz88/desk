package com.chenxinzhi.utils

import com.chenxinzhi.model.base.KuWoResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
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
            register(
                ContentType.Text.Plain, KotlinxSerializationConverter(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            )

        }


    }


    suspend inline fun <reified T> getAndFallBack(
        fallbackKuWoResponse: T?,
        builder: HttpRequestBuilder.() -> Unit,
    ): T? {


        return try {
            CLIENT.get(builder)
                .body()
        } catch (e: Exception) {
            fallbackKuWoResponse
        }


    }

    suspend inline fun <reified T> postAndFallBack(
        fallbackKuWoResponse: KuWoResponse<T> = KuWoResponse(),
        builder: HttpRequestBuilder.() -> Unit,
    ): KuWoResponse<T> {
        return try {
            CLIENT.post(builder)
                .body()
        } catch (e: Exception) {
            fallbackKuWoResponse
        }
    }
}