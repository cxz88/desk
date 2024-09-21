package com.chenxinzhi.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KuWoResponse<out T>(
    @SerialName("code")
    val code: Int = 999,
    @SerialName("msg")
    val msg: String = "网络错误",
    @SerialName("reqId")
    val reqId: String = "",
    @SerialName("tId")
    val tId: String? = null,
    @SerialName("data")
    val data: T? = null,

    @SerialName("profileId")
    val profileId: String = "",
    @SerialName("curTime")
    val curTime: Long = 0L,
    @SerialName("success")
    val success: Boolean = false,
)