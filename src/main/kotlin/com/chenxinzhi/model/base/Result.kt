package com.chenxinzhi.model.base

import kotlinx.serialization.SerialName

data class Response<out T>(
    @SerialName("code")
    val code: Int,
    @SerialName("result")
    val result: T
)