package com.chenxinzhi.model.lyc


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lyc(
    @SerialName("code")
    val code: Int,
    @SerialName("curTime")
    val curTime: Long,
    @SerialName("data")
    val `data`: Data,
    @SerialName("msg")
    val msg: String,
    @SerialName("profileId")
    val profileId: String,
    @SerialName("reqId")
    val reqId: String,
    @SerialName("success")
    val success: Boolean
)

@Serializable
data class Data(
    @SerialName("lrclist")
    val lrclist: List<Lrclist>
)

@Serializable
data class Lrclist(
    @SerialName("lineLyric")
    val lineLyric: String,
    @SerialName("time")
    val time: String
)