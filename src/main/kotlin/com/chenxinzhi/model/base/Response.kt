package com.chenxinzhi.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<out T>(
    @SerialName("code")
    val code: Int,
    @SerialName("result")
    val result: T? = null
)